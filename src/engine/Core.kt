package engine

import engine.entity.EntityManager
import engine.input.Input
import engine.system.SystemManager
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

object Core {
	var state: GameState = GameState.Stopped
		private set

	var deltaTime: Double = 0.0
		private set

	var time: Double = 0.0
		private set

	private var requestId: Int = -1


	val canvas = document.getElementById("game") as HTMLCanvasElement
	val canvasContext = canvas.getContext("2d") as CanvasRenderingContext2D

	private var fpsTime = 0.0
	private var fpsCount = 0
	private var average = 0.0

	private fun requestUpdate() {
		requestId = window.requestAnimationFrame {
			this.deltaTime = (it - time) / 1000.0
			this.time = it

			if (deltaTime < 5.0)
				update(deltaTime)

			requestUpdate()
		}
	}


	private fun update(deltaTime: Double) {
		Input.update()
		canvasContext.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
		SystemManager.update(deltaTime)

		fps(deltaTime)
		entityCount()
	}

	private fun entityCount() {
		canvasContext.font = "16px Verdana"
		canvasContext.fillStyle = "#000000"
		canvasContext.fillText("${EntityManager.entityCount} entities", 0.0, 40.0)
	}

	private fun fps(deltaTime: Double) {
		fpsTime += deltaTime
		fpsCount++


		if (fpsTime > 0.5) {
			average = kotlin.math.round(1.0 / (fpsTime / fpsCount))
			fpsTime = 0.0
			fpsCount = 0
		}

		canvasContext.font = "20px Verdana"
		canvasContext.fillStyle = "#000000"
		canvasContext.fillText("$average fps", 0.0, 20.0)
	}

	fun run() {
		if (state == GameState.Running)
			throw RuntimeException("Already in running state")

		this.state = GameState.Running
		this.time = window.performance.now()
		this.deltaTime = 0.0

		requestUpdate()
	}

	fun stop() {
		if (state != GameState.Running)
			throw RuntimeException("Not in running state")

		window.cancelAnimationFrame(requestId)
	}
}