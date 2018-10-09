import ecs.component.*
import ecs.system.*
import engine.Core
import engine.entity.EntityManager
import engine.system.SystemManager
import org.w3c.dom.HTMLCanvasElement
import utility.Double2
import utility.Image
import utility.Rgba
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Math


fun main(args: Array<String>) {
    val canvas = document.getElementById("game") as HTMLCanvasElement
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight

    SystemManager.registerSystem(MoveSystem())
    SystemManager.registerSystem(SpriteRendererSystem(), 100)
    SystemManager.registerSystem(CircleRenderSystem(), 100)
    SystemManager.registerSystem(RectangleRotationRenderSystem(), 100)
    SystemManager.registerSystem(UserMoveSystem())
    SystemManager.registerSystem(RoundAndRoundWeGoSystem())

    EntityManager.createEntity(PositionComponent(canvas.width / 2.0, canvas.height / 2.0),
            RenderRectangleComponent(50.0, 75.0, Rgba.GREEN),
            RotationComponent(45.0),
            RotateMeComponent(10.0))


    Image.newInstance("./img/test.png") {
        EntityManager.createEntity(PositionComponent(80.0, 50.0), SpriteComponent(it), UserControlledComponent(), VelocityComponent(100.0, 100.0))
    }

    val halfWidth = canvas.width / 2.0
    val halfHeight = canvas.height / 2.0

    for (i in 1..100) {
        val x = Math.random() * canvas.width
        val y = Math.random() * canvas.height
        val velocity = Double2(halfWidth - x, halfHeight - y).normalized
        velocity.x *= 3.0
        velocity.y *= 3.0
        EntityManager.createEntity(PositionComponent(x, y), RenderCircleComponent(Math.random() * 10, Rgba.BLUE), VelocityComponent(velocity))
    }


    Core.run()
}