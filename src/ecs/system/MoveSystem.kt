package ecs.system

import Matter
import ecs.component.PhysicsEntityComponent
import ecs.component.UserControlledComponent
import engine.entity.Entity
import engine.input.Input
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude


class UserMoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		if (horizontalInput == 0.0 && verticalInput == 0.0)
			return

		entities.forEach {
			val physicsEntityComponent = it.getComponent(PhysicsEntityComponent::class)
			val userControlledComponent = it.getComponent(UserControlledComponent::class)

			val velocity = physicsEntityComponent.body.velocity
			val acceleration = userControlledComponent.acceleration

			velocity.x += horizontalInput * deltaTime * acceleration.x
			velocity.y += verticalInput * deltaTime * acceleration.y

			Matter.Body.setVelocity(physicsEntityComponent.body, velocity)
			Matter.Sleeping.set(physicsEntityComponent.body, false)
		}
	}


	override val requirements = ECInclusionNode(UserControlledComponent::class)
			.andInclude(PhysicsEntityComponent::class)
}