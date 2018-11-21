package ecs.system.render

import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.physics.PhysicsKinematicEntityComponent
import engine.entity.Entity
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import engine.system.requirements.andInclude
import engine.system.requirements.orInclude


class PhysicsRenderSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(PhysicsDynamicEntityComponent::class)
			.orInclude(PhysicsKinematicEntityComponent::class)
			.andInclude(GraphicsComponent::class)
			.andInclude(PhysicsEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsComponent = it.getComponent<PhysicsEntityComponent>()
			val graphicsComponent = it.getComponent<GraphicsComponent>()
			val body = physicsComponent.body

			graphicsComponent.value.apply {
				rotation = body.angleRadians
				position.x = body.position.x
				position.y = body.position.y
			}
		}
	}
}