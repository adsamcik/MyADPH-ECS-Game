package engine

import engine.events.UpdateManager
import engine.graphics.Graphics
import engine.graphics.UserInterface
import engine.input.Input
import engine.physics.Physics
import kotlin.browser.window

object Core {
	var state: GameState = GameState.Stopped
		private set

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
	}


	private fun requestUpdate() {
		requestId = window.requestAnimationFrame {
			val scaledToSeconds = it / 1000.0
			this.deltaTime = scaledToSeconds - time
			this.time = scaledToSeconds


			if (deltaTime <= 1.0)
				UpdateManager.update(deltaTime)

			requestUpdate()
		}
	}

	fun run() {
		if (state == GameState.Running)
			throw RuntimeException("Already in running state")

		this.state = GameState.Running
		this.time = window.performance.now() / 1000.0
		this.deltaTime = 0.0

		requestUpdate()
	}

	fun stop() {
		if (state != GameState.Running)
			throw RuntimeException("Not in running state")

		window.cancelAnimationFrame(requestId)
	}
}