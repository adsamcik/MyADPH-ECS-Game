package ecs.system

import ecs.components.RotateMeComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude

class RoundAndRoundWeGoSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsEntityComponent = it.getComponent<PhysicsEntityComponent>()
			val rotateMeComponent = it.getComponent<RotateMeComponent>()


			physicsEntityComponent.body.rotate(rotateMeComponent.speed * deltaTime)
		}
	}

	override val requirements = ECInclusionNode(RotateMeComponent::class).andInclude(PhysicsEntityComponent::class)
}