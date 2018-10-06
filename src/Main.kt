
import ecs.component.PositionComponent
import ecs.component.RenderCircleComponent
import ecs.component.VelocityComponent
import ecs.system.MoveSystem
import ecs.system.RenderSystem
import engine.Core
import engine.entity.EntityManager
import engine.system.SystemManager
import org.w3c.dom.HTMLCanvasElement
import utility.Rgba
import kotlin.browser.document
import kotlin.browser.window

fun main(args: Array<String>) {
    val canvas = document.getElementById("game") as HTMLCanvasElement
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight

    SystemManager.registerSystem(MoveSystem())
    SystemManager.registerSystem(RenderSystem())
    EntityManager.createEntity(PositionComponent(0.0, 0.0), VelocityComponent(8.0, 8.0), RenderCircleComponent(10.0, Rgba.GREEN))


    Core.run()
}