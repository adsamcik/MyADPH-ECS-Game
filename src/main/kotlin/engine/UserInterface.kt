package engine

import engine.entity.EntityManager
import engine.interfaces.IUpdatable
import jslib.pixi.Point
import jslib.pixi.Text
import jslib.pixi.TextStyle
import org.w3c.dom.events.Event
import kotlin.browser.window

object UserInterface : IUpdatable {
	private var fpsTime = 0.0
	private var fpsCount = 0
	private var average = 0.0


	private var entityText: Text
	private var fpsText: Text

	init {
		val style = TextStyle().apply {
			fontFamily = "Verdana"
			fontSize = 16
			fill = "#FFFFFF"
			stroke = "#000000"
			strokeThickness = 2
		}

		entityText = Text("", style)
		Graphics.uiContainer.addChild(entityText)

		fpsText = Text("", style)
		fpsText.position = Point(0, 20)
		Graphics.uiContainer.addChild(fpsText)

		UpdateManager.subscribe(this)

		window.onresize = this::onResize
		onResize(null)
	}

	private fun onResize(event: Event?) {
		val view = Graphics.pixi.view
		Graphics.uiContainer.position.set(-view.width / 2, -view.height / 2)
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

	override fun update(deltaTime: Double) {
		entityCount()
		fps(deltaTime)
	}
}