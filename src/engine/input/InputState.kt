package engine.input

import utility.Double2

data class InputState(var pointer: Pointer = Pointer(),
                      var keyStates: MutableMap<String, KeyState> = mutableMapOf()) {

	fun registerKeyDown(key: String) {
		keyStates[key] = KeyState.Down
	}

	fun registerKeyUp(key: String) {
		keyStates[key] = KeyState.Up
	}

	fun registerPointDown(timeStamp: Long, x: Int, y: Int) {
		pointer.state = KeyState.Down
		pointer.position = Double2(x, y)
		pointer.lastUpdate = timeStamp
		pointer.deltaVector.x = 0.0
		pointer.deltaVector.y = 0.0
		pointer.deltaVelocity = 0.0
	}

	fun registerPointerUp(timeStamp: Long, x: Int, y: Int) {
		pointer.state = KeyState.Up

		val position = Double2(x, y)
		val deltaTime = (timeStamp - pointer.lastUpdate) / 1000
		val vector = position - pointer.position
		pointer.deltaVector = vector.normalized
		pointer.deltaVelocity = position.magnitude / deltaTime
		pointer.lastUpdate = timeStamp
		pointer.position = position
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
data class Pointer(var position: Double2 = Double2(0.0, 0.0),
                   var state: KeyState = KeyState.Free) {
	var lastUpdate: Long = 0
	var deltaVector: Double2 = Double2()
	var deltaVelocity: Double = 0.0
}