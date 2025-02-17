package engine.graphics.ui

import debug.Debug
import debug.DebugLevel
import engine.entity.EntityManager
import engine.events.UpdateManager
import engine.events.IUpdatable
import engine.graphics.Graphics
import definition.jslib.pixi.Point
import definition.jslib.pixi.Text
import definition.jslib.pixi.TextStyle
import engine.types.Rgba

object UserInterface : IUpdatable {
	private var fpsTime = 0.0
	private var fpsCount = 0
	private var average = 0.0

	private var entityText: Text
	private var fpsText: Text

	private val energyBar = definition.jslib.pixi.Graphics()
	private val healthBar = definition.jslib.pixi.Graphics()

	private const val BAR_WIDTH = 150.0
	private const val BAR_HEIGHT = 30.0

	//Debug initialization
	init {
		val style = TextStyle().apply {
			fontFamily = "Verdana"
			fontSize = 16
			fill = "#FFFFFF"
			stroke = "#000000"
			strokeThickness = 2
		}

		entityText = Text("", style)
		Graphics.staticUIContainer.addChild(entityText)

		fpsText = Text("", style)
		fpsText.position = Point(0, 20)
		Graphics.staticUIContainer.addChild(fpsText)

		UpdateManager.subscribe(this)
	}

	//Player info initialization
	init {
		energyBar.beginFill(Rgba.YELLOW.rgb)
		energyBar.lineStyle(2, Rgba.BLACK.rgb)
		energyBar.drawRect(0, 44,
			BAR_WIDTH,
			BAR_HEIGHT
		)
		Graphics.staticUIContainer.addChild(energyBar)


		healthBar.beginFill(Rgba.RED.rgb)
		healthBar.lineStyle(2, Rgba.BLACK.rgb)
		healthBar.drawRect(0, 78,
			BAR_WIDTH,
			BAR_HEIGHT
		)
		Graphics.staticUIContainer.addChild(healthBar)

		hideUI()
	}

	/*private fun onResize(event: Event?) {
		val view = Graphics.pixi.view
		Graphics.uiContainer.position.set(-view.width / 2, -view.height / 2)
	}*/

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

	fun updateEnergy(energy: Double, maxEnergy: Double) {
		val energyPercentage = energy / maxEnergy
		energyBar.width = energyPercentage * BAR_WIDTH
	}

	fun updateHealth(health: Double, maxHealth: Double) {
		val healthPercentage = health / maxHealth
		healthBar.width = healthPercentage * BAR_WIDTH
	}

	fun hideUI() {
		energyBar.visible = false
		healthBar.visible = false
	}

	fun showUI() {
		energyBar.visible = true
		healthBar.visible = true
	}

	override fun update(deltaTime: Double) {
		if (Debug.isActive(DebugLevel.CRITICAL)) {
			entityCount()
			fps(deltaTime)
		}
	}
}