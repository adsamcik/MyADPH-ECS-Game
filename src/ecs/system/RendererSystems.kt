package ecs.system

import ecs.component.*
import engine.Core
import engine.entity.Entity
import engine.system.ComponentInclusion
import engine.system.ComponentRequirement
import engine.system.ISystem
import org.w3c.dom.CanvasRenderingContext2D
import kotlin.math.PI

class CircleRenderSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = it.getComponent(PositionComponent::class)
            val renderComponent = it.getComponent(RenderCircleComponent::class)

            ctx.fillStyle = renderComponent.color.rgbaString
            ctx.beginPath()
            ctx.arc(positionComponent.x, positionComponent.y, renderComponent.radius, 0.0, PI * 2.0)
            ctx.closePath()
            ctx.fill()
            ctx.stroke()
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(RenderCircleComponent::class, ComponentInclusion.MustHave))

}

class SpriteRendererSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val spriteComponent = it.getComponent(SpriteComponent::class)
            val positionComponent = it.getComponent(PositionComponent::class)
            ctx.drawImage(spriteComponent.image, positionComponent.x, positionComponent.y)
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(SpriteComponent::class, ComponentInclusion.MustHave))
}

class RectangleRenderSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = it.getComponent(PositionComponent::class)
            val renderComponent = it.getComponent(RenderRectangleComponent::class)

            ctx.beginPath()
            ctx.rect(positionComponent.x, positionComponent.y, renderComponent.width, renderComponent.height)
            ctx.closePath()
            ctx.fillStyle = renderComponent.color.rgbaString
            ctx.fill()
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(RenderRectangleComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(RotationComponent::class, ComponentInclusion.MustNotHave))

}

class RectangleRotationRenderSystem : ISystem {
    private val ctx: CanvasRenderingContext2D = Core.canvasContext

    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = it.getComponent(PositionComponent::class)
            val rotationComponent = it.getComponent(RotationComponent::class)
            val renderComponent = it.getComponent(RenderRectangleComponent::class)

            val rotationAngleRadians = rotationComponent.rotation * kotlin.math.PI / 180.0
            ctx.fillStyle = renderComponent.color.rgbaString

            ctx.translate(positionComponent.x + 0.5 * renderComponent.width, positionComponent.y + 0.5 * renderComponent.height)
            ctx.rotate(rotationAngleRadians)
            ctx.fillRect(-0.5 * renderComponent.width, -0.5 * renderComponent.height, renderComponent.width, renderComponent.height)
            ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        }
    }

    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(RenderRectangleComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(RotationComponent::class, ComponentInclusion.MustHave))

}