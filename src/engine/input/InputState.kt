package engine.input

import jslib.ZingTouch

data class InputState(var gestures: Gestures = Gestures(),
                      var keyStates: MutableMap<String, KeyState> = mutableMapOf()) {

	fun registerKeyDown(key: String) {
		keyStates[key] = KeyState.Down
	}

	fun registerKeyUp(key: String) {
		keyStates[key] = KeyState.Up
	}

	fun registerPan(panEvent: ZingTouch.Pan.EventData) {
		gestures.pan = panEvent
	}

	fun registerSwipe(swipeEvent: ZingTouch.Swipe.EventData) {
		gestures.swipe = swipeEvent
	}

	fun getState(vararg keys: String): KeyState {
		var result = KeyState.Free
		keys.forEach {
			result = result.or(keyStates[it])
		}

		return result
	}

	fun update(changeState: InputState) {
		changeState.keyStates.forEach {
			val previousState = keyStates[it.key]
			if (previousState == it.value) {
				if (previousState == KeyState.Down)
					keyStates[it.key] = KeyState.Pressed
				else if (previousState == KeyState.Up)
					keyStates[it.key] = KeyState.Free
			} else {
				if (previousState == KeyState.Free && it.value == KeyState.Down ||
						previousState == KeyState.Pressed && it.value == KeyState.Up ||
						previousState == null)
					keyStates[it.key] = it.value

			}
		}

		gestures.update(changeState.gestures)
		changeState.gestures.clear()

	}
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