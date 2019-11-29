package engine.input

import definition.jslib.ZingTouch
import org.w3c.dom.events.KeyboardEvent

data class InputState(
	var gestures: Gestures = Gestures(),
	var keyStates: MutableMap<String, KeyData> = mutableMapOf()
) {

	fun registerKeyDown(event: KeyboardEvent) {
		keyStates[event.code] = KeyData(event, KeyState.Down)
	}

	fun registerKeyUp(event: KeyboardEvent) {
		keyStates[event.code] = KeyData(event, KeyState.Up)
	}

	fun registerPan(panEvent: ZingTouch.Pan.EventData) {
		gestures.pan = panEvent
	}

	fun registerSwipe(swipeEvent: ZingTouch.Swipe.EventData) {
		gestures.swipe = swipeEvent
	}

	fun getKeyData(key: String): KeyData {
		return keyStates[key] ?: KeyData(key, KeyState.Free, ctrl = false, alt = false)
	}

	fun getState(key: String): KeyState {
		return keyStates[key]?.state ?: KeyState.Free
	}

	fun getState(vararg keys: String): KeyState {
		var result = KeyState.Free
		keys.forEach {
			val stateData = keyStates[it]
			if (stateData != null) {
				result = result.or(stateData.state)
			}
		}

		return result
	}

	fun update(changeState: InputState) {
		keyStates.forEach {
			if (it.value.state == KeyState.Up) {
				keyStates.remove(it.key)
			} else if (it.value.state == KeyState.Down) {
				it.value.state = KeyState.Pressed
			}
		}


		changeState.keyStates.forEach {
			val previousState = keyStates[it.key]

			if (previousState == null)
				keyStates[it.key] = it.value
			else {
				when {
					it.value.isDown -> {
						if (previousState.isUp) {
							keyStates[it.key] = it.value
						}
					}
					it.value.isUp -> {
						if (previousState.isDown) {
							keyStates[it.key] = it.value
						}
					}
				}
			}
		}

		changeState.keyStates.clear()

		gestures.update(changeState.gestures)
		changeState.gestures.clear()
	}
}

data class KeyData(val key: String, var state: KeyState, val ctrl: Boolean, val alt: Boolean) {
	val isUp get() = state.isUp()
	val isDown get() = state.isDown()
	val isJustPressed get() = state == KeyState.Down
	val isJustUp get() = state == KeyState.Up

	constructor(event: KeyboardEvent, state: KeyState) : this(event.key, state, event.ctrlKey, event.altKey)
}

enum class KeyState {
	Free {
		override fun and(keyState: KeyState): KeyState = when (keyState) {
			Down, Pressed -> Down
			else -> Free
		}
	},
	Down {
		override fun and(keyState: KeyState): KeyState = when (keyState) {
			Up, Free -> Up
			else -> Pressed
		}
	},
	Pressed {
		override fun and(keyState: KeyState): KeyState = when (keyState) {
			Up, Free -> Up
			else -> Pressed
		}
	},
	Up {
		override fun and(keyState: KeyState): KeyState = when (keyState) {
			Down, Pressed -> Down
			else -> Free
		}
	};

	abstract fun and(keyState: KeyState): KeyState

	fun or(keyState: KeyState): KeyState {
		return when {
			this == Down || keyState == Down -> Down
			this == Up || keyState == Up -> Up
			this == Free && keyState == Free -> Free
			else -> Pressed
		}
	}

	fun or(keyState: KeyState?): KeyState {
		return if (keyState == null)
			this
		else
			or(keyState)
	}

	fun isDown() = this == Pressed || this == Down

	fun isUp() = this == Free || this == Up

	companion object {
		fun or(first: KeyState?, second: KeyState?): KeyState {
			return when {
				first == null -> second ?: Free
				second == null -> first
				else -> first.or(second)
			}
		}
	}
}

/**
 * Touch or mouse devices
 */
class Gestures {
	var pan: ZingTouch.Pan.EventData? = null
	var swipe: ZingTouch.Swipe.EventData? = null

	fun clear() {
		pan = null
		swipe = null
	}

	fun update(gestures: Gestures) {
		pan = gestures.pan
		swipe = gestures.swipe
	}
}