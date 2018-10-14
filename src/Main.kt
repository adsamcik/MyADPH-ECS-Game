import PIXI.Container
import ecs.component.GraphicsComponent
import ecs.component.InitializePhysicsComponent
import ecs.component.PhysicsEngineComponent
import ecs.component.UserControlledComponent
import ecs.system.*
import engine.Core
import engine.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import engine.physics.Circle
import engine.physics.Rectangle
import engine.system.SystemManager
import utility.Double2
import utility.Rgba
import kotlin.browser.window
import kotlin.random.Random


fun buildEntity(world: Matter.World, container: Container, builder: BodyBuilder): Entity {
	val (body, graphics) = builder.build()
	container.addChild(graphics)
	return EntityManager.createEntity(InitializePhysicsComponent(world, body), GraphicsComponent(graphics))
}

fun main(args: Array<String>) {
	SystemManager.registerSystems(
			Pair(UserMoveSystem(), -1),
			Pair(RoundAndRoundWeGoSystem(), 0),
			Pair(BoundSystem(), 50),
			Pair(CircleRendererSystem(), 100),
			Pair(MatterEngineUpdateSystem(), -60),
			Pair(PhysicsInitializationSystem(), -1000)
	)

	val physicsEngine = Matter.Engine.create()

	EntityManager.createEntity(PhysicsEngineComponent(physicsEngine))


	val entity = buildEntity(physicsEngine.world, Graphics.dynamicContainer, BodyBuilder()
			.setShape(Circle(10.0))
			.setFillColor(Rgba.BLUE)
			.setPosition(70.0, 50.0)
			.setLineWidth(3.0)
			.setFriction(0.1)
			.setFrictionAir(0.0)
			.setFrictionStatic(0.3))

	EntityManager.addComponent(entity, UserControlledComponent())

	val width = window.innerWidth
	val height = window.innerHeight

	val halfWidth = window.innerWidth / 2.0
	val halfHeight = window.innerHeight / 2.0


	val builder = BodyBuilder()
			.setFillColor(Rgba.WHITE)
			.setElasticity(0.5)
			.setFriction(0.01)
			.setFrictionAir(0.0)

	for (i in 1..25) {
		val x = Random.nextDouble() * width
		val y = Random.nextDouble() * height
		val velocity = Double2(halfWidth - x, halfHeight - y).normalized
		velocity.x *= Random.nextDouble(10.0)
		velocity.y *= Random.nextDouble(10.0)

		val widthNormalized = kotlin.math.abs(x - halfWidth) / halfWidth
		val heightNormalized = kotlin.math.abs(y - halfHeight) / halfHeight

		val radius = kotlin.math.min(widthNormalized, heightNormalized) * 20.0 + 10.0

		val shape = Circle(radius)
		builder.setShape(shape).setPosition(x, y)

		val (body, graphics) = builder.build()

		Graphics.dynamicContainer.addChild(graphics)

		Matter.Body.setVelocity(body, velocity)
		EntityManager.createEntity(
				GraphicsComponent(graphics),
				InitializePhysicsComponent(physicsEngine.world, body)
		)

	}

	val color = Rgba(145U, 0U, 0U)

	buildEntity(physicsEngine.world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(width.toDouble(), 40.0))
			.setFillColor(color)
			.setPosition(halfWidth, height - 20.0)
			.setStatic(true)
			.setElasticity(1.0))


	buildEntity(physicsEngine.world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(width.toDouble(), 40.0))
			.setFillColor(color)
			.setPosition(halfWidth, 20.0)
			.setStatic(true)
			.setElasticity(1.0))

	buildEntity(physicsEngine.world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(40.0, height.toDouble()))
			.setFillColor(color)
			.setPosition(20.0, halfHeight)
			.setStatic(true)
			.setElasticity(1.0))

	buildEntity(physicsEngine.world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(40.0, height.toDouble()))
			.setFillColor(color)
			.setPosition(width - 20.0, halfHeight)
			.setStatic(true)
			.setElasticity(1.0))

	Core.run()
}