package engine.input

import engine.Core
import engine.events.UpdateManager
import engine.events.IUpdatable

object InterfaceInputChecker : IUpdatable {
	init {
		UpdateManager.subscribePost(this)
	}

	override fun update(deltaTime: Double) {
		if(Input.getKeyState(Input.ESCAPE) == KeyState.Down)
			Core.pauseUnpause()
	}

}