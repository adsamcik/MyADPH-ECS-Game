package engine

import PIXI.Point
import PIXI.Text
import engine.entity.EntityManager
import engine.input.Input
import engine.system.SystemManager
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


	val pixi = PIXI.Application(window.innerWidth, window.innerHeight)

	private var fpsTime = 0.0
	private var fpsCount = 0
	private var average = 0.0


	private var entityText: Text
	private var fpsText: Text

	init {
		document.body!!.appendChild(pixi.view)

		val style = PIXI.TextStyle().apply {
			fontFamily = "Verdana"
			fontSize = 16
			fill = "#FFFFFF"
			stroke = "#000000"
			strokeThickness = 1.0
		}

		entityText = Text("", style)
		pixi.stage.addChild(entityText)

		fpsText = Text("", style)
		fpsText.position = Point(0, 20)
		pixi.stage.addChild(fpsText)
	}

	private fun requestUpdate() {
		requestId = window.requestAnimationFrame {
			this.deltaTime = (it - time) / 1000.0
			this.time = it


			if (deltaTime <= 0.2)
				update(deltaTime)

			requestUpdate()
		}
	}


	private fun update(deltaTime: Double) {
		Input.update()
		SystemManager.update(deltaTime)

		fps(deltaTime)
		entityCount()
	}

	private fun entityCount() {
		entityText.text = "${EntityManager.entityCount} entities"
	}

	private fun fps(deltaTime: Double) {
		fpsTime += deltaTime
		fpsCount++


		if (fpsTime > 0.5) {
			average = kotlin.math.round(1.0 / (fpsTime / fpsCount))
			fpsTime = 0.0
			fpsCount = 0
		}

		fpsText.text = "$average fps"
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