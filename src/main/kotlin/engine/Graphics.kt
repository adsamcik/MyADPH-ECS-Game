package engine

import engine.physics.Physics
import engine.physics.bodies.BodyMotionType
import engine.physics.engines.PlanckPhysicsEngine
import jslib.Matter
import jslib.pixi.Application
import jslib.pixi.Container
import jslib.planck
import org.w3c.dom.events.Event
import utility.Double2
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
		if (Physics.DEBUG)
			initializeDebugRenderer()
		else
			initializeStandardRenderer()

		pixi.stage.addChild(staticBackgroundContainer)
		pixi.stage.addChild(dynamicContainer)
		pixi.stage.addChild(staticForegroundContainer)
		pixi.stage.addChild(uiContainer)


		uiContainer.interactive = false

		onResize()
		window.onresize = this::onResize

		pixi.renderer.autoResize = true
	}

	fun getContainer(motionType: BodyMotionType) = when (motionType) {
		BodyMotionType.Static -> Graphics.staticBackgroundContainer
		BodyMotionType.Kinematic -> Graphics.staticForegroundContainer
		BodyMotionType.Dynamic -> Graphics.dynamicContainer
	}

	fun centerAt(center: Double2) {
		//pixi.stage.pivot.set(center.x, center.y)
		staticBackgroundContainer.pivot.set(center.x, center.y)
		dynamicContainer.pivot.set(center.x, center.y)
		staticForegroundContainer.pivot.set(center.x, center.y)

		/*centerContainer(staticBackgroundContainer)
		centerContainer(dynamicContainer)
		centerContainer(staticForegroundContainer)*/
		this.center = center
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

	fun initializeStandardRenderer() {
		document.body!!.appendChild(pixi.view)
	}

	fun initializeDebugRenderer() {
		when (Physics.engineType) {
			Physics.EngineType.Matterjs -> initializeMatterDebug()
			Physics.EngineType.Planckjs -> initializePlanckDebug()
		}
	}

	fun initializePlanckDebug() {
		planck.testbed {
			it.speed = 1.3
			it.hz = 50

			it.width = window.innerWidth
			it.height = window.innerHeight
			//it.x = window.innerWidth / 100.0
			//it.y = window.innerHeight / 100.0
			return@testbed (Physics.engine as PlanckPhysicsEngine).world
		}
	}

	fun initializeMatterDebug() {
		val options = js(
			"{" +
					"    element: document.body,\n" +
					"    options: {\n" +
					"        pixelRatio: 1,\n" +
					"        background: '#fafafa',\n" +
					"        wireframeBackground: '#222',\n" +
					"        hasBounds: false,\n" +
					"        enabled: true,\n" +
					"        wireframes: true,\n" +
					"        showSleeping: true,\n" +
					"        showDebug: true,\n" +
					"        showBroadphase: false,\n" +
					"        showBounds: false,\n" +
					"        showVelocity: true,\n" +
					"        showCollisions: true,\n" +
					"        showSeparations: true,\n" +
					"        showAxes: true,\n" +
					"        showPositions: false,\n" +
					"        showAngleIndicator: true,\n" +
					"        showIds: false,\n" +
					"        showShadows: false,\n" +
					"        showVertexNumbers: false,\n" +
					"        showConvexHulls: false,\n" +
					"        showInternalEdges: false,\n" +
					"        showMousePosition: false\n" +
					"    }\n" +
					"}"
		)
		options.engine = Physics.engine
		options.options.width = window.innerWidth
		options.options.height = window.innerHeight

		val render = Matter.Render.create(options)
		Matter.Render.run(render)
	}
}