package engine.graphics

import engine.physics.bodies.BodyMotionType
import general.Double2
import jslib.pixi.Application
import jslib.pixi.Container
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window

object Graphics {

	val pixi = Application(window.innerWidth, window.innerHeight, object {
		val antialias = true
	})

	val dynamicContainer = Container()

	val staticBackgroundContainer = Container()

	val staticForegroundContainer = Container()

	val uiContainer = Container()

	var scale = 1.0
		private set

	var center: Double2 = Double2()
		private set

	init {
		initializeRenderer()

		pixi.stage.addChild(staticBackgroundContainer)
		pixi.stage.addChild(dynamicContainer)
		pixi.stage.addChild(staticForegroundContainer)
		pixi.stage.addChild(uiContainer)


		uiContainer.interactive = false

		onResize()
		window.onresize = this::onResize

		pixi.renderer.autoDensity = true
		center = Double2(window.innerWidth / 2.0, window.innerHeight / 2.0)
	}

	fun getContainer(motionType: BodyMotionType) = when (motionType) {
		BodyMotionType.Static -> staticBackgroundContainer
		BodyMotionType.Kinematic -> staticForegroundContainer
		BodyMotionType.Dynamic -> dynamicContainer
	}

	fun centerAt(center: Double2) {
		//pixi.stage.pivot.set(center.x, center.y)
		staticBackgroundContainer.pivot.set(center.x, center.y)
		dynamicContainer.pivot.set(center.x, center.y)
		staticForegroundContainer.pivot.set(center.x, center.y)

		/*centerContainer(staticBackgroundContainer)
		centerContainer(dynamicContainer)
		centerContainer(staticForegroundContainer)*/
		Graphics.center = center
	}

	private fun centerContainer(container: Container) {
		container.position.set(
			pixi.renderer.width.toDouble() / 2.0 * container.scale.x.toDouble(),
			pixi.renderer.height.toDouble() / 2.0 * container.scale.y.toDouble()
		)
	}

	private fun onResize(event: Event? = null) {
		pixi.apply {
			renderer.resize(window.innerWidth, window.innerHeight)

			val max = kotlin.math.max(view.height, view.width).toDouble()
			scale = max / 250.0

			staticBackgroundContainer.scale.set(scale, scale)
			dynamicContainer.scale.set(scale, scale)
			staticForegroundContainer.scale.set(scale, scale)

			centerContainer(stage)
		}
	}

	fun initializeRenderer() {
		document.body!!.appendChild(pixi.view)
	}
}