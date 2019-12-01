package engine.graphics

import engine.events.GraphicsEventManager
import engine.events.ResizeEventData
import engine.physics.bodies.BodyMotionType
import game.levels.ILevelLoadListener
import game.levels.LevelManager
import general.Double2
import general.Int2
import definition.jslib.pixi.Application
import definition.jslib.pixi.Container
import definition.jslib.pixi.Rectangle
import kotlin.browser.document
import kotlin.browser.window
import general.Double2.Companion.set
import kotlin.dom.addClass
import kotlin.js.json

object Graphics : ILevelLoadListener {

	val pixi = Application(window.innerWidth, window.innerHeight, json("antialias" to "true"))

	private val worldContainer = Container()

	val dynamicContainer = Container()

	val staticBackgroundContainer = Container()

	val staticForegroundContainer = Container()

	val worldUIContainer = Container()


	private val uiContainer = Container()

	val staticUIContainer = Container()

	val levelUIContainer = Container()

	val backgroundUIContainer = Container().apply { interactive = true }

	var scale = 1.0
		private set

	var center: Double2 = Double2()
		private set

	var dimensions: Int2 = Int2()
		private set

	var screenCenter: Int2 = Int2()
		private set

	init {
		initializeRenderer()

		pixi.stage.apply {
			addChild(backgroundUIContainer)
			addChild(worldContainer)
			worldContainer.addChild(staticBackgroundContainer)
			worldContainer.addChild(dynamicContainer)
			worldContainer.addChild(staticForegroundContainer)
			worldContainer.addChild(worldUIContainer)
			addChild(uiContainer)
			uiContainer.addChild(levelUIContainer)
			uiContainer.addChild(staticUIContainer)
		}

		onResize(ResizeEventData(Int2(window.innerWidth, window.innerHeight)))

		GraphicsEventManager.onResize(this::onResize)

		pixi.renderer.autoDensity = true

		LevelManager.subscribeLoad(this)
	}

	override fun onAfterLevelUnload() {
		levelUIContainer.removeChildren()
		staticBackgroundContainer.removeChildren()
		staticForegroundContainer.removeChildren()
		dynamicContainer.removeChildren()
	}

	fun getContainer(motionType: BodyMotionType) = when (motionType) {
		BodyMotionType.Static -> staticBackgroundContainer
		BodyMotionType.Kinematic -> staticForegroundContainer
		BodyMotionType.Dynamic -> dynamicContainer
	}

	fun centerAt(center: Double2) {
		worldContainer.pivot.set(center.x, center.y)

		this.center = center
	}

	private fun centerContainer(container: Container) {
		container.position.set(
			pixi.renderer.width.toDouble() / 2.0 * container.scale.x,
			pixi.renderer.height.toDouble() / 2.0 * container.scale.y
		)
	}

	private fun onResize(eventData: ResizeEventData) {
		val dimensions = eventData.dimensions
		this.dimensions = dimensions
		this.screenCenter = (dimensions / 2.0).roundToInt2()

		pixi.apply {
			renderer.resize(dimensions.x, dimensions.y)

			val max = kotlin.math.max(view.height, view.width).toDouble()
			scale = max / 250.0

			worldContainer.scale.set(scale, scale)

			centerContainer(stage)
		}

		val leftUpperCorner = Double2(-dimensions.x / 2.0, -dimensions.y / 2.0)
		uiContainer.position.set(leftUpperCorner)
		backgroundUIContainer.position.set(leftUpperCorner)
		backgroundUIContainer.hitArea = Rectangle(0, 0, dimensions.x, dimensions.y)
	}

	private fun initializeRenderer() {
		requireNotNull(document.body).appendChild(pixi.view)
		pixi.view.addClass("game-view")
	}
}