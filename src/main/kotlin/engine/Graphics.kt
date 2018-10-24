package engine

import jslib.Matter
import jslib.pixi.Application
import jslib.pixi.Container
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

	init {
		if (PhysicsEngine.DEBUG)
			initializeDebugRenderer()
		else
			initializeStandardRenderer()

		pixi.stage.addChild(staticBackgroundContainer)
		pixi.stage.addChild(dynamicContainer)
		pixi.stage.addChild(staticForegroundContainer)
		pixi.stage.addChild(uiContainer)

		uiContainer.interactive = false
	}

	fun initializeStandardRenderer() {
		document.body!!.appendChild(pixi.view)
	}

	fun initializeDebugRenderer() {
		val options = js(
			"{\n" +
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
		options.engine = PhysicsEngine.engine
		options.options.width = window.innerWidth
		options.options.height = window.innerHeight

		val render = Matter.Render.create(options)
		Matter.Render.run(render)
	}
}