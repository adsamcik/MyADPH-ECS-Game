package ecs.system

import ecs.components.PhysicsDynamicEntityComponent
import ecs.components.PhysicsEntityComponent
import ecs.components.PlayerComponent
import engine.entity.Entity
import engine.input.Input
import engine.system.ISystem
import jslib.Matter
import utility.Double2
import utility.ECInclusionNode
import utility.INode
import utility.andInclude


class UserKeyboardMoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		if (horizontalInput == 0.0 && verticalInput == 0.0)
			return

		entities.forEach {
			val physicsEntityComponent = it.getComponent(PhysicsEntityComponent::class)

			val velocity = physicsEntityComponent.body.velocity

			velocity.x += horizontalInput * deltaTime * 20
			velocity.y += verticalInput * deltaTime * 30

			Matter.Body.setVelocity(physicsEntityComponent.body, velocity)
			Matter.Sleeping.set(physicsEntityComponent.body, false)
		}
	}


	override val requirements = ECInclusionNode(PlayerComponent::class)
			.andInclude(PhysicsEntityComponent::class).andInclude(PhysicsDynamicEntityComponent::class)
}

class UserTouchMoveSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PlayerComponent::class)
			.andInclude(PhysicsEntityComponent::class).andInclude(PhysicsDynamicEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		if (Input.hasSwiped) {
			val swipe = Input.swipeData
			val radians = swipe.currentDirection * kotlin.math.PI / 180.0
			val directionVector = Double2(kotlin.math.cos(radians), kotlin.math.sin(radians))
			directionVector.y = -directionVector.y
			val velocityVector = directionVector * swipe.velocity

			entities.forEach {
				val physics = it.getComponent(PhysicsEntityComponent::class)
				val velocity = Double2()
				velocity.x = 3.0 * velocityVector.x + physics.body.velocity.x
				velocity.y = 10.0 * velocityVector.y + physics.body.velocity.y
				velocity.coerceAtMost(10.0)

				Matter.Body.setVelocity(physics.body, velocity.toVector())
				Matter.Sleeping.set(physics.body, false)
			}
		}
	}

}