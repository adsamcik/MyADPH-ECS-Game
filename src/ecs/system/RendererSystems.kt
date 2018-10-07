package ecs.system

import ecs.component.PositionComponent
import ecs.component.RenderCircleComponent
import ecs.component.RenderRectangleComponent
import ecs.component.SpriteComponent
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ComponentRequirement
import engine.system.ISystem
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

class CircleRenderSystem : ISystem {
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

class SpriteRendererSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val spriteComponent = EntityManager.getComponent(it, SpriteComponent::class.js)
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            ctx.drawImage(spriteComponent.image, positionComponent.x, positionComponent.y)
        }
    }

    override fun componentSpecification(): Collection<ComponentRequirement> = requirement

    companion object {
        val requirement = listOf(
                ComponentRequirement(PositionComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave),
                ComponentRequirement(SpriteComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave))
    }

}

class RectangleRenderSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val renderComponent = EntityManager.getComponent(it, RenderRectangleComponent::class.js)

            ctx.beginPath()
            ctx.rect(positionComponent.x, positionComponent.y, renderComponent.width, renderComponent.height)
            ctx.fillStyle = renderComponent.color.rgbaString
            ctx.fill()
        }
    }

    override fun componentSpecification(): Collection<ComponentRequirement> = requirement

    companion object {
        val requirement = listOf(
                ComponentRequirement(PositionComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave),
                ComponentRequirement(RenderRectangleComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave))
    }

}