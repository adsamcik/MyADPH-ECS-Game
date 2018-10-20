
import ecs.components.GraphicsComponent
import ecs.components.InitializePhysicsComponent
import ecs.components.PhysicsDynamicEntityComponent
import ecs.components.UserControlledComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import ecs.components.modifiers.PendingModifierReceiverComponent
import ecs.eventsystem.ModifierEventSystem
import ecs.system.*
import engine.Core
import engine.Graphics
import engine.PhysicsEngine
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import engine.physics.Circle
import engine.physics.Rectangle
import engine.system.SystemManager
import game.modifiers.ModifierCommandFactory
import game.modifiers.ShapeModifierFactory
import jslib.Matter
import jslib.pixi.Container
import utility.Double2
import utility.Rgba
import kotlin.browser.window
import kotlin.random.Random


fun buildEntity(world: Matter.World, container: Container, builder: BodyBuilder): Entity {
	val (body, graphics) = builder.build()
	container.addChild(graphics)
	return EntityManager.createEntity(InitializePhysicsComponent(world, body), GraphicsComponent(graphics))
}

fun initializeSystems() {
	SystemManager.registerSystems(
			Pair(UserKeyboardMoveSystem(), -1),
			Pair(UserTouchMoveSystem(), -1),
			Pair(RoundAndRoundWeGoSystem(), 0),
			//Pair(BoundSystem(), 50),
			Pair(RendererSystem(), 100),
			Pair(MatterEngineUpdateSystem(), -60),
			Pair(PhysicsInitializationSystem(), -1000),
			Pair(ModifierUpdateSystem(), 0),
			Pair(InitializeModifierReceiverSystem(), -999),
			Pair(ModifierAddSystem(), -900)
	)
}

fun generatePlayerBodyBuilder() = BodyBuilder()
		.setShape(Circle(10.0))
		.setFillColor(Rgba.BLUE)
		.setPosition(70.0, 50.0)
		.setLineWidth(3.0)
		.setFriction(0.1)
		.setFrictionAir(0.0)
		.setFrictionStatic(0.3)

fun initializePlayer() {
	val playerBodyBuilder = generatePlayerBodyBuilder()
	val entity = buildEntity(PhysicsEngine.world, Graphics.dynamicContainer, playerBodyBuilder)

	EntityManager.addComponents(entity, UserControlledComponent(20.0, 30.0), PendingModifierReceiverComponent(playerBodyBuilder), PhysicsDynamicEntityComponent())
}

lateinit var modifierEventSystem: ModifierEventSystem

fun main(args: Array<String>) {

	initializeSystems()
	initializePlayer()


	val world = PhysicsEngine.world

	world.bounds.min.x = 0.0
	world.bounds.min.y = 0.0
	world.bounds.max.x = window.innerWidth.toDouble()
	world.bounds.max.y = window.innerHeight.toDouble()

	console.log(world)

	modifierEventSystem = ModifierEventSystem(PhysicsEngine.eventManager)


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

		Matter.Body.setVelocity(body, velocity.toVector())
		EntityManager.createEntity(
				GraphicsComponent(graphics),
				InitializePhysicsComponent(world, body),
				PhysicsDynamicEntityComponent()
		)

	}

	val color = Rgba(145U, 0U, 0U)

	buildEntity(world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(width.toDouble(), 200.0))
			.setFillColor(color)
			.setPosition(halfWidth, height.toDouble() + 80.0)
			.setStatic(true)
			.setElasticity(0.4))


	val topBarrierEntity = buildEntity(world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(width.toDouble(), 200.0))
			.setFillColor(color)
			.setPosition(halfWidth, -80.0)
			.setStatic(true)
			.setElasticity(0.4))


	val squareBody = generatePlayerBodyBuilder().setShape(Rectangle(40.0, 40.0))

	val shapeSpreader = ModifierCommandFactory().addModifier(ShapeModifierFactory().setBodyBuilder(squareBody).setTimeLeft(5.0).setEntity(topBarrierEntity))
	EntityManager.addComponent(topBarrierEntity, ModifierSpreaderComponent(shapeSpreader))

	buildEntity(world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(200.0, height.toDouble()))
			.setFillColor(color)
			.setPosition(-80.0, halfHeight)
			.setStatic(true)
			.setElasticity(0.4))

	buildEntity(world, Graphics.staticContrainer, BodyBuilder()
			.setShape(Rectangle(200.0, height.toDouble()))
			.setFillColor(color)
			.setPosition(width + 80.0, halfHeight)
			.setStatic(true)
			.setElasticity(0.4))

	Core.run()
}