package engine.graphics

import debug.Debug
import debug.DebugLevel
import engine.entity.EntityManager
import engine.events.UpdateManager
import engine.interfaces.IUpdatable
import jslib.pixi.Point
import jslib.pixi.Text
import jslib.pixi.TextStyle
import org.w3c.dom.events.Event
import engine.types.Rgba
import kotlin.browser.window

object UserInterface : IUpdatable {
	private var fpsTime = 0.0
	private var fpsCount = 0
	private var average = 0.0

	private var entityText: Text
	private var fpsText: Text

	private val energyBar = jslib.pixi.Graphics()
	private val healthBar = jslib.pixi.Graphics()

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
		Graphics.uiContainer.addChild(entityText)

		fpsText = Text("", style)
		fpsText.position = Point(0, 20)
		Graphics.uiContainer.addChild(fpsText)

		UpdateManager.subscribe(this)

		window.onresize = this::onResize
		onResize(null)
	}

	//Player info initialization
	init {
		energyBar.beginFill(Rgba.YELLOW.rgb)
		energyBar.lineStyle(2, Rgba.BLACK.rgb)
		energyBar.drawRect(0, 44, BAR_WIDTH, BAR_HEIGHT)
		Graphics.uiContainer.addChild(energyBar)


		healthBar.beginFill(Rgba.RED.rgb)
		healthBar.lineStyle(2, Rgba.BLACK.rgb)
		healthBar.drawRect(0, 78, BAR_WIDTH, BAR_HEIGHT)
		Graphics.uiContainer.addChild(healthBar)

		hideUI()
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
		if (Debug.shouldLog(DebugLevel.CRITICAL)) {
			entityCount()
			fps(deltaTime)
		}
	}
}