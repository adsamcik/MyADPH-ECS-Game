package engine

import engine.events.UpdateManager
import engine.graphics.PausedMenu
import engine.input.Input
import engine.input.InterfaceInputChecker

//PATTERN State
enum class GameState {
	Running {
		override fun onEnterState() {}
		override fun onExitState() {}

		override fun update(deltaTime: Double) {
			UpdateManager.update(deltaTime)
		}
	},
	Paused {
		override fun onExitState() {
			PausedMenu.hide()
		}

		override fun onEnterState() {
			PausedMenu.show()
		}

		override fun update(deltaTime: Double) {
			Input.update(deltaTime)
			InterfaceInputChecker.update(deltaTime)
		}
	},
	Stopped {
		override fun onEnterState() {}

		override fun onExitState() {}

		override fun update(deltaTime: Double) {}
	};

	abstract fun onEnterState()
	abstract fun onExitState()
	abstract fun update(deltaTime: Double)
}