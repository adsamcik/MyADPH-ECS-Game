package ecs.system

import ecs.component.PositionComponent
import ecs.component.UserControlledComponent
import ecs.component.VelocityComponent
import engine.entity.Entity
import engine.input.Input
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude

class MoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val positionComponent = it.getComponent(PositionComponent::class)
			val velocityComponent = it.getComponent(VelocityComponent::class)

			positionComponent.x += velocityComponent.x * deltaTime
			positionComponent.y += velocityComponent.y * deltaTime
		}
	}


	override val requirements = ECInclusionNode(PositionComponent::class)
			.andInclude(VelocityComponent::class)
}

class UserMoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		if (horizontalInput == 0.0 && verticalInput == 0.0)
			return

		entities.forEach {
			val velocityComponent = it.getComponent(VelocityComponent::class)

			velocityComponent.x += horizontalInput * deltaTime * 150.0
			velocityComponent.y += verticalInput * deltaTime * 150.0
		}
	}


	override val requirements = ECInclusionNode(UserControlledComponent::class)
			.andInclude(VelocityComponent::class)
}