
import ecs.component.InitializePhysicsComponent
import ecs.component.PhysicsEngineComponent
import ecs.component.PositionComponent
import ecs.component.UserControlledComponent
import ecs.system.*
import engine.Core
import engine.entity.EntityManager
import engine.physics.Circle
import engine.physics.Rectangle
import engine.system.SystemManager
import org.w3c.dom.HTMLCanvasElement
import utility.Double2
import utility.Image
import utility.Rgba
import kotlin.browser.document
import kotlin.browser.window
import kotlin.random.Random


fun main(args: Array<String>) {
	val canvas = document.getElementById("game") as HTMLCanvasElement
	canvas.width = window.innerWidth
	canvas.height = window.innerHeight

	SystemManager.registerSystems(
			Pair(UserMoveSystem(), -1),
			Pair(RoundAndRoundWeGoSystem(), 0),
			Pair(BoundSystem(), 50),
			Pair(SpriteRendererSystem(), 100),
			Pair(CircleRenderSystem(), 100),
			Pair(RectangleRotationRenderSystem(), 100),
			Pair(PhysicsRenderSystem(), 100),
			Pair(MatterEngineUpdateSystem(), -60),
			Pair(PhysicsInitializationSystem(), -1000)
	)

	val physicsEngine = Matter.Engine.create()

	EntityManager.createEntity(PhysicsEngineComponent(physicsEngine))


	Image.newInstance("./img/test.png") {
		EntityManager.createEntity(
				PositionComponent(80.0, 50.0),
				InitializePhysicsComponent(physicsEngine.world, Circle(10.0), Render().apply { fillStyle = Rgba.RED.rgbaString }),
				UserControlledComponent())
	}

	val halfWidth = canvas.width / 2.0
	val halfHeight = canvas.height / 2.0

	for (i in 1..100) {
		val x = Random.nextDouble() * canvas.width
		val y = Random.nextDouble() * canvas.height
		val velocity = Double2(halfWidth - x, halfHeight - y).normalized
		velocity.x *= Random.nextDouble(40.0)
		velocity.y *= Random.nextDouble(40.0)

		val widthNormalized = kotlin.math.abs(x - halfWidth) / halfWidth
		val heightNormalized = kotlin.math.abs(y - halfHeight) / halfHeight

		val radius = kotlin.math.min(widthNormalized, heightNormalized) * 20.0 + 10.0

		val body = Render()
		body.fillStyle = Rgba.BLUE.rgbaString

		val shape = Circle(radius)

		EntityManager.createEntity(
				PositionComponent(x, y),
				InitializePhysicsComponent(physicsEngine.world, shape, body)
		)

	}

	val body = Render()
	body.fillStyle = Rgba.GREEN.rgbaString

	EntityManager.createEntity(PositionComponent(canvas.width / 2.0, canvas.height - 20.0),
			InitializePhysicsComponent(physicsEngine.world, Rectangle(canvas.width.toDouble(), 40.0), body, true))

	EntityManager.createEntity(PositionComponent(10.0, canvas.height / 2.0),
			InitializePhysicsComponent(physicsEngine.world, Rectangle(20.0, canvas.height.toDouble()), body, true))

	EntityManager.createEntity(PositionComponent(canvas.width - 10.0, canvas.height / 2.0),
			InitializePhysicsComponent(physicsEngine.world, Rectangle(20.0, canvas.height.toDouble()), body, true))

	Core.run()
}