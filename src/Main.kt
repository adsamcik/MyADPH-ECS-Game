
import ecs.component.InitializePhysicsComponent
import ecs.component.PhysicsEngineComponent
import ecs.component.UserControlledComponent
import ecs.system.*
import engine.Core
import engine.entity.EntityManager
import engine.physics.BodyBuilder
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
				InitializePhysicsComponent(physicsEngine.world,
						BodyBuilder()
								.setShape(Circle(10.0))
								.setFillColor(Rgba.RED)
								.setPosition(70.0, 50.0)
								.setLineWidth(3.0)
								.build()),
				UserControlledComponent())
	}

	console.log(Rgba.RED.rgbaString)

	val halfWidth = canvas.width / 2.0
	val halfHeight = canvas.height / 2.0


	val builder = BodyBuilder().setFillColor(Rgba.BLUE).setElasticity(0.5)

	for (i in 1..100) {
		val x = Random.nextDouble() * canvas.width
		val y = Random.nextDouble() * canvas.height
		val velocity = Double2(halfWidth - x, halfHeight - y).normalized
		velocity.x *= Random.nextDouble(40.0)
		velocity.y *= Random.nextDouble(40.0)

		val widthNormalized = kotlin.math.abs(x - halfWidth) / halfWidth
		val heightNormalized = kotlin.math.abs(y - halfHeight) / halfHeight

		val radius = kotlin.math.min(widthNormalized, heightNormalized) * 20.0 + 10.0

		builder.setShape(Circle(radius)).setPosition(x, y)

		EntityManager.createEntity(
				InitializePhysicsComponent(physicsEngine.world, builder.build())
		)

	}

	val color = Rgba.GREEN

	EntityManager.createEntity(
			InitializePhysicsComponent(physicsEngine.world, BodyBuilder()
					.setShape(Rectangle(canvas.width.toDouble(), 40.0))
					.setFillColor(color)
					.setPosition(canvas.width / 2.0, canvas.height - 20.0)
					.setStatic(true)
					.setElasticity(1.0)
					.build()))

	EntityManager.createEntity(InitializePhysicsComponent(physicsEngine.world, BodyBuilder()
			.setShape(Rectangle(20.0, canvas.height.toDouble()))
			.setFillColor(color)
			.setPosition(10.0, canvas.height / 2.0)
			.setStatic(true)
			.setElasticity(1.0)
			.build()))

	EntityManager.createEntity(InitializePhysicsComponent(physicsEngine.world, BodyBuilder()
			.setShape(Rectangle(20.0, canvas.height.toDouble()))
			.setFillColor(color)
			.setPosition(canvas.width - 10.0, canvas.height / 2.0)
			.setStatic(true)
			.setElasticity(1.0)
			.build()))

	Core.run()
}