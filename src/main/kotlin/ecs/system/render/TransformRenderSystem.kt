package ecs.system.render

import ecs.components.GraphicsComponent
import ecs.components.TransformComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsKinematicEntityComponent
import engine.entity.Entity
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import engine.system.requirements.andInclude
import engine.system.requirements.orInclude


class TransformRenderSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(PhysicsDynamicEntityComponent::class)
			.orInclude(PhysicsKinematicEntityComponent::class)
			.andInclude(GraphicsComponent::class)
			.andInclude(TransformComponent::class)

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