package engine

import engine.graphics.Graphics
import engine.graphics.ui.UserInterface
import engine.input.Input
import engine.input.InterfaceInputChecker
import engine.physics.Physics
import tests.Assert
import kotlin.browser.window

object Core {
	var state: GameState = GameState.Stopped
		private set(value) {
			field.onExitState()
			value.onEnterState()
			field = value
		}

	var deltaTime: Double = 0.0
		private set

	var time: Double = 0.0
		private set

	private var requestId: Int = -1

	init {
		//Initializes singletons
		Input
		UserInterface
		Physics
		Graphics
		InterfaceInputChecker
	}


	private fun requestUpdate() {
		requestId = window.requestAnimationFrame {
			val scaledToSeconds = it / 1000.0
			this.deltaTime = scaledToSeconds - time
			this.time = scaledToSeconds


			state.update(deltaTime)

			requestUpdate()
		}
	}

	fun pauseUnpause() {
		when (state) {
			GameState.Running -> pause()
			GameState.Paused -> unpause()
			GameState.Stopped -> throw RuntimeException("Cannot unpause from stopped state")
		}
	}

	fun run() {
		if (state == GameState.Running) {
			throw RuntimeException("Already in running state")
		} else if (state == GameState.Paused) {
			throw RuntimeException("Cannot run from paused state")
		}

		this.state = GameState.Running
		this.time = window.performance.now() / 1000.0
		this.deltaTime = 0.0

		requestUpdate()
	}

	fun unpause() {
		Assert.equals(GameState.Paused, state)
		this.state = GameState.Running
	}

	fun pause() {
		if (state == GameState.Paused) throw RuntimeException("Already in paused state")

		this.state = GameState.Paused
	}

	fun stop() {
		if (state != GameState.Running) throw RuntimeException("Not in running state")

		window.cancelAnimationFrame(requestId)
	}
}