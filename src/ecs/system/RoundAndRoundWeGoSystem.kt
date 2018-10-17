package ecs.system

import ecs.components.RotateMeComponent
import ecs.components.RotationComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude

class RoundAndRoundWeGoSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val rotationComponent = it.getComponent(RotationComponent::class)
			val rotateMeComponent = it.getComponent(RotateMeComponent::class)

			rotationComponent.rotation += rotateMeComponent.speed * deltaTime
		}
	}

	override val requirements = ECInclusionNode(RotateMeComponent::class).andInclude(RotationComponent::class)
}