package ecs.system

import ecs.component.*
import engine.Core
import engine.entity.Entity
import engine.system.ISystem
import org.w3c.dom.CanvasRenderingContext2D
import utility.ECInclusionNode
import utility.INode
import utility.andExclude
import utility.andInclude
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

	override val requirements = ECInclusionNode(PositionComponent::class).andInclude(RenderCircleComponent::class)

}

class SpriteRendererSystem : ISystem {
	private val ctx: CanvasRenderingContext2D = Core.canvasContext

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val renderComponent = it.getComponent(RenderSpriteComponent::class)
			val positionComponent = it.getComponent(PositionComponent::class)
			ctx.translate(-0.5 * renderComponent.image.width, -0.5 * renderComponent.image.height)
			ctx.drawImage(renderComponent.image, positionComponent.x, positionComponent.y)
			ctx.translate(0.5 * renderComponent.image.width, 0.5 * renderComponent.image.height)
		}
	}

	override val requirements = ECInclusionNode(PositionComponent::class).andInclude(RenderSpriteComponent::class)
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

	override val requirements = ECInclusionNode(PositionComponent::class).andInclude(RenderRectangleComponent::class).andExclude(RotationComponent::class)
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

	override val requirements = ECInclusionNode(PositionComponent::class).andInclude(RenderRectangleComponent::class).andInclude(RotationComponent::class)

}

class PhysicsRenderSystem : ISystem {
	private val ctx: CanvasRenderingContext2D = Core.canvasContext

	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsComponent = it.getComponent(PhysicsEntityComponent::class)
			val vertices = physicsComponent.body.vertices

			ctx.beginPath()

			ctx.moveTo(vertices[0].x, vertices[0].y)

			for (j in 0 until vertices.size)
				ctx.lineTo(vertices[j].x, vertices[j].y)

			ctx.lineTo(vertices[0].x, vertices[0].y)

			val render = physicsComponent.body.render

			ctx.fillStyle = render.fillStyle
			ctx.fill()
			if (render.lineWidth > 0) {
				ctx.lineWidth = render.lineWidth
				ctx.strokeStyle = render.strokeStyle
				ctx.stroke()
			}
		}
	}

}