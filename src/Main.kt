import ecs.component.*
import ecs.system.*
import engine.Core
import engine.entity.EntityManager
import engine.system.BoundSystem
import engine.system.SystemManager
import org.w3c.dom.HTMLCanvasElement
import utility.Circle
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
			Pair(MoveSystem(), 0),
			Pair(UserMoveSystem(), 0),
			Pair(RoundAndRoundWeGoSystem(), 0),
			Pair(BoundSystem(), 50),
			Pair(SpriteRendererSystem(), 100),
			Pair(CircleRenderSystem(), 100),
			Pair(RectangleRotationRenderSystem(), 100),
			Pair(PhysicsSystem(), -50)
	)

	EntityManager.createEntity(PositionComponent(canvas.width / 2.0, canvas.height / 2.0),
			RenderRectangleComponent(50.0, 75.0, Rgba.GREEN),
			RotationComponent(45.0),
			RotateMeComponent(10.0))


	Image.newInstance("./img/test.png") {
		EntityManager.createEntity(
				PositionComponent(80.0, 50.0),
				RenderSpriteComponent(it),
				UserControlledComponent(),
				VelocityComponent(100.0, 100.0),
				PhysicsComponent(1.0, true, 10.0))
	}

	val halfWidth = canvas.width / 2.0
	val halfHeight = canvas.height / 2.0

	for (i in 1..200) {
		val x = Random.nextDouble() * canvas.width
		val y = Random.nextDouble() * canvas.height
		val velocity = Double2(halfWidth - x, halfHeight - y).normalized
		velocity.x *= Random.nextDouble(40.0)
		velocity.y *= Random.nextDouble(40.0)

		val widthNormalized = kotlin.math.abs(x - halfWidth) / halfWidth
		val heightNormalized = kotlin.math.abs(y - halfHeight) / halfHeight

		val radius = kotlin.math.min(widthNormalized, heightNormalized) * 7.0 + 3.0

		val collider = ColliderComponent(Circle(radius))

		EntityManager.createEntity(
				PositionComponent(x, y),
				RenderCircleComponent(radius, Rgba.BLUE),
				VelocityComponent(velocity),
				PhysicsComponent(Random.nextDouble(2.0), Random.nextBoolean(), 10.0),
				collider
		)

	}

	Core.run()
}