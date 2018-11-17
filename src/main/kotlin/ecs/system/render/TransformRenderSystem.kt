package ecs.system.render

import ecs.components.GraphicsComponent
import ecs.components.TransformComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsKinematicEntityComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude
import utility.orInclude


class TransformRenderSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(TransformComponent::class)
			.andInclude(GraphicsComponent::class)
			.andInclude(PhysicsDynamicEntityComponent::class)
			.orInclude(PhysicsKinematicEntityComponent::class)

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