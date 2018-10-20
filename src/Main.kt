
import ecs.components.PhysicsEntityComponent
import ecs.eventsystem.ModifierEventSystem
import ecs.system.*
import engine.Core
import engine.Graphics
import engine.PhysicsEngine
import engine.physics.BodyBuilder
import engine.physics.Circle
import engine.physics.Rectangle
import engine.system.SystemManager
import game.levels.EntityCreator
import game.modifiers.ShapeModifierFactory
import jslib.Matter
import utility.Double2
import utility.Rgba
import kotlin.browser.window
import kotlin.random.Random

fun initializeSystems() {
	SystemManager.registerSystems(
			Pair(UserKeyboardMoveSystem(), -1),
			Pair(UserTouchMoveSystem(), -1),
			Pair(RoundAndRoundWeGoSystem(), 0),
			//Pair(BoundSystem(), 50),
			Pair(RendererSystem(), 100),
			Pair(MatterEngineUpdateSystem(), -60),
			Pair(ModifierUpdateSystem(), 0),
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

	EntityCreator().apply {
		setBodyBuilder(playerBodyBuilder)
		setPlayer(true)
		setReceiveModifiers(true)
	}.create(Graphics.dynamicContainer)
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

	modifierEventSystem = ModifierEventSystem(PhysicsEngine.eventManager)


	val width = window.innerWidth
	val height = window.innerHeight

	val halfWidth = window.innerWidth / 2.0
	val halfHeight = window.innerHeight / 2.0


	val builder = BodyBuilder()
			.setFillColor(Rgba.WHITE)
			.setRestitution(0.5)
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


		val entity = EntityCreator().apply {
			setBodyBuilder(builder)
		}.create(Graphics.dynamicContainer)


		val body = entity.getComponent(PhysicsEntityComponent::class).body
		Matter.Body.setVelocity(body, velocity.toVector())

	}

	val color = Rgba(145U, 0U, 0U)

	EntityCreator().apply {
		setBodyBuilder(BodyBuilder()
				.setShape(Rectangle(width.toDouble(), 200.0))
				.setFillColor(color)
				.setPosition(halfWidth, height.toDouble() + 80.0)
				.setStatic(true)
				.setRestitution(0.4))
	}.create()

	EntityCreator().apply {
		setBodyBuilder(BodyBuilder()
				.setShape(Rectangle(width.toDouble(), 200.0))
				.setFillColor(color)
				.setPosition(halfWidth, -80.0)
				.setStatic(true)
				.setRestitution(0.4))
	}.create()

	val squareBody = generatePlayerBodyBuilder().setShape(Rectangle(40.0, 40.0))

	EntityCreator().apply {
		setBodyBuilder(BodyBuilder()
				.setShape(Rectangle(200.0, height.toDouble()))
				.setFillColor(color)
				.setPosition(-80.0, halfHeight)
				.setStatic(true)
				.setRestitution(0.4))
		addModifier(ShapeModifierFactory().apply {
			setBodyBuilder(squareBody)
			setTimeLeft(5.0)
		})
	}.create()

	EntityCreator().apply {
		setBodyBuilder(BodyBuilder()
				.setShape(Rectangle(200.0, height.toDouble()))
				.setFillColor(color)
				.setPosition(width + 80.0, halfHeight)
				.setStatic(true)
				.setRestitution(0.4))
	}.create()

	Core.run()
}