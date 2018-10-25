package ecs.system

import ecs.components.PhysicsEntityComponent
import ecs.components.RotateMeComponent
import engine.entity.Entity
import engine.system.ISystem
import jslib.Matter
import utility.ECInclusionNode
import utility.andInclude

class RoundAndRoundWeGoSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val rotationComponent = it.getComponent(PhysicsEntityComponent::class)
			val rotateMeComponent = it.getComponent(RotateMeComponent::class)

			Matter.Body.rotate(rotationComponent.body, rotateMeComponent.speed * deltaTime)
		}
	}

	override val requirements = ECInclusionNode(RotateMeComponent::class).andInclude(PhysicsEntityComponent::class)
}