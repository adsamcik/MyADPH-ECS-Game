package game.editor.system

import engine.events.IUpdatable
import engine.input.Input
import engine.input.KeyState

class EditorShortcutSystem(
	private val deleteCallback: () -> Unit,
	private val copyCallback: () -> Unit,
	private val pasteCallback: () -> Unit
) : IUpdatable {
	override fun update(deltaTime: Double) {
		val deleteState = Input.getKeyState(Input.DELETE)
		if (deleteState == KeyState.Down) {
			deleteCallback()
		}

		val copyState = Input.getKeyData(Input.C)

		if (copyState.isJustPressed && copyState.ctrl) {
			copyCallback()
		}

		val pasteState = Input.getKeyData(Input.V)

		if (pasteState.isJustPressed && pasteState.ctrl) {
			pasteCallback()
		}

	}
}