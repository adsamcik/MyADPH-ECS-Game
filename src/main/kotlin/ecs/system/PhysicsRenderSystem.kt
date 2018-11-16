package ecs.system

import ecs.components.GraphicsComponent
import ecs.components.TransformComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude


class PhysicsRenderSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEntityComponent::class)
		.andInclude(GraphicsComponent::class)
		.andInclude(PhysicsDynamicEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsComponent = it.getComponent<PhysicsEntityComponent>()
			val graphicsComponent = it.getComponent<GraphicsComponent>()
			val body = physicsComponent.body

			graphicsComponent.value.apply {
				rotation = body.angleRadians
				x = body.position.x
				y = body.position.y
			}
		}
	}
}

class TransformRenderSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(TransformComponent::class)
			.andInclude(GraphicsComponent::class)
			.andInclude(PhysicsDynamicEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val transformComponent = it.getComponent<TransformComponent>()
			val graphicsComponent = it.getComponent<GraphicsComponent>()

			graphicsComponent.value.apply {
				rotation = transformComponent.angleRadians
				x = transformComponent.position.x
				y = transformComponent.position.y
			}
		}
	}
}