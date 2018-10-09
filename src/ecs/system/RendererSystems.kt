package ecs.system

import ecs.component.*
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ComponentInclusion
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

            ctx.fillStyle = renderComponent.color.rgbaString
            ctx.beginPath()
            ctx.arc(positionComponent.x, positionComponent.y, renderComponent.radius, 0.0, PI * 2.0)
            ctx.closePath()
            ctx.fill()
            ctx.stroke()
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(RenderCircleComponent::class.js, ComponentInclusion.MustHave))

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

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(SpriteComponent::class.js, ComponentInclusion.MustHave))
}

class RectangleRenderSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val renderComponent = EntityManager.getComponent(it, RenderRectangleComponent::class.js)

            ctx.beginPath()
            ctx.rect(positionComponent.x, positionComponent.y, renderComponent.width, renderComponent.height)
            ctx.closePath()
            ctx.fillStyle = renderComponent.color.rgbaString
            ctx.fill()
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(RenderRectangleComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(RotationComponent::class.js, ComponentInclusion.MustNotHave))

}

class RectangleRotationRenderSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val rotationComponent = EntityManager.getComponent(it, RotationComponent::class.js)
            val renderComponent = EntityManager.getComponent(it, RenderRectangleComponent::class.js)

            val rotationAngleRadians = rotationComponent.rotation * kotlin.math.PI / 180.0
            ctx.fillStyle = renderComponent.color.rgbaString

            ctx.translate(positionComponent.x + 0.5 * renderComponent.width, positionComponent.y + 0.5 * renderComponent.height)
            ctx.rotate(rotationAngleRadians)
            ctx.fillRect(-0.5 * renderComponent.width, -0.5 * renderComponent.height, renderComponent.width, renderComponent.height)
            ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(RenderRectangleComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(RotationComponent::class.js, ComponentInclusion.MustHave))

}