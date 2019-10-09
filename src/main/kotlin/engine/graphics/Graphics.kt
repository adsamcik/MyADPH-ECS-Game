package engine.graphics

import engine.events.GraphicsEventManager
import engine.events.ResizeEventData
import engine.physics.bodies.BodyMotionType
import game.levels.ILevelLoadListener
import game.levels.LevelManager
import general.Double2
import general.Int2
import jslib.pixi.Application
import jslib.pixi.Container
import kotlin.browser.document
import kotlin.browser.window

object Graphics : ILevelLoadListener {

	val pixi = Application(window.innerWidth, window.innerHeight, object {
		val antialias = true
	})

	val dynamicContainer = Container()

	val staticBackgroundContainer = Container()

	val staticForegroundContainer = Container()

	private val uiContainer = Container()

	val staticUIContainer = Container()

	val levelUIContainer = Container()

	var scale = 1.0
		private set

	var center: Double2 = Double2()
		private set

	var dimensions: Int2 = Int2()
		private set

	init {
		initializeRenderer()

		pixi.stage.apply {
			addChild(staticBackgroundContainer)
			addChild(dynamicContainer)
			addChild(staticForegroundContainer)
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
		staticBackgroundContainer.pivot.set(center.x, center.y)
		dynamicContainer.pivot.set(center.x, center.y)
		staticForegroundContainer.pivot.set(center.x, center.y)

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
		center = dimensions / 2.0

		pixi.apply {
			renderer.resize(dimensions.x, dimensions.y)

			val max = kotlin.math.max(view.height, view.width).toDouble()
			scale = max / 250.0

			staticBackgroundContainer.scale.set(scale, scale)
			dynamicContainer.scale.set(scale, scale)
			staticForegroundContainer.scale.set(scale, scale)

			centerContainer(stage)
		}

		uiContainer.position.set(-center.x, -center.y)
	}

	fun initializeRenderer() {
		requireNotNull(document.body).appendChild(pixi.view)
	}
}