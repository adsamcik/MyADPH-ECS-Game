package engine.input

import utility.Double2

data class InputState(var mouse: Mouse = Mouse(),
                      var keyStates: MutableMap<String, KeyState> = mutableMapOf()) {

	fun registerKeyDown(key: String) {
		keyStates[key] = KeyState.Down
	}

	fun registerKeyUp(key: String) {
		keyStates[key] = KeyState.Up
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
	Free,
	Down,
	Pressed,
	Up;

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

data class Mouse(var double2: Double2 = Double2(0.0, 0.0),
                 var mouseLeft: KeyState = KeyState.Free,
                 var mouseRight: KeyState = KeyState.Free)