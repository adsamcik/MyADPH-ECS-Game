package ecs.system

import ecs.component.PositionComponent
import ecs.component.RenderCircleComponent
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ComponentRequirement
import engine.system.ISystem
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

class CircleRendererSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val renderComponent = EntityManager.getComponent(it, RenderCircleComponent::class.js)

            ctx.beginPath()
            ctx.arc(positionComponent.x, positionComponent.y, renderComponent.radius, 0.0, PI * 2.0)
            ctx.fillStyle = renderComponent.color.rgbaString
            ctx.fill()
            ctx.stroke()
            ctx.closePath()
        }
    }

    override fun componentSpecification(): Collection<ComponentRequirement> = requirement

    companion object {
        val requirement = listOf(
                ComponentRequirement(PositionComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave),
                ComponentRequirement(RenderCircleComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave))
    }

}