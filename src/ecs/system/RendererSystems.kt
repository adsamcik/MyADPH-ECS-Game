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

class CollisionRenderSystem : ISystem {
	private val ctx: CanvasRenderingContext2D = Core.canvasContext

	override val requirements: INode<Entity> = ECInclusionNode(DynamicColliderComponent::class).andInclude(PositionComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		ctx.fillStyle = "Red"

		entities.forEach {
			val position = it.getComponent(PositionComponent::class)
			val collider = it.getComponent(DynamicColliderComponent::class)

			val collisionData = collider.collisionData
			if (collisionData != null) {
				ctx.beginPath()
				ctx.arc(collisionData.point.x, collisionData.point.y, 2.0, 0.0, PI * 2.0)
				ctx.closePath()
				ctx.fill()

				collider.collisionData = null
			}
		}
	}

}

class VelocityRenderSystem : ISystem {
	private val ctx: CanvasRenderingContext2D = Core.canvasContext

	override val requirements: INode<Entity> = ECInclusionNode(VelocityComponent::class).andInclude(PositionComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		ctx.fillStyle = "Green"

		entities.forEach {
			val position = it.getComponent(PositionComponent::class)
			val velocity = it.getComponent(VelocityComponent::class)

			ctx.beginPath()
			ctx.moveTo(position.x, position.y)
			ctx.lineTo(position.x + velocity.x, position.y+velocity.y)
			ctx.closePath()
			ctx.stroke()
		}
	}

}