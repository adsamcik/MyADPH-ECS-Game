package ecs.system

import ecs.components.AccelerationComponent
import ecs.components.EnergyComponent
import ecs.components.PlayerComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Core
import engine.entity.Entity
import engine.input.Input
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import engine.system.requirements.andExclude
import engine.system.requirements.andInclude
import general.Double2


class DevMoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		if (horizontalInput == 0.0 && verticalInput == 0.0)
			return

		val horizontalAcceleration = horizontalInput * deltaTime * 2
		val verticalAcceleration = verticalInput * deltaTime * 6.8


		entities.forEach {
			val physicsEntityComponent = it.getComponent<PhysicsEntityComponent>()
			val body = physicsEntityComponent.body

			val velocity = body.velocity

			velocity.x += horizontalAcceleration
			velocity.y += verticalAcceleration


			body.applyForce(Double2(horizontalAcceleration, verticalAcceleration))
			body.position = Double2()
		}
	}


	override val requirements = ECInclusionNode(PlayerComponent::class)
		.andInclude(PhysicsEntityComponent::class)
		.andInclude(PhysicsDynamicEntityComponent::class)
		.andExclude(EnergyComponent::class)
}

class KeyboardMoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		if (horizontalInput == 0.0 && verticalInput == 0.0)
			return

		val horizontalAcceleration = horizontalInput * deltaTime
		var verticalAcceleration = verticalInput * deltaTime

		val deltaForce = (deltaTime * -verticalInput).coerceAtLeast(0.0)

		entities.forEach {
			if (deltaForce > 0) {
				val energyComponent = it.getComponent<EnergyComponent>()

				val deltaDraw = deltaForce * energyComponent.currentDraw * energyComponent.maxEnergyUsage
				if (energyComponent.energy < deltaDraw)
					verticalAcceleration = 0.0
				else {
					energyComponent.currentDraw = (energyComponent.currentDraw + deltaTime).coerceAtMost(1.0)
					energyComponent.energy -= deltaDraw
					energyComponent.lastUseTime = Core.time
				}
			}

			val physicsEntityComponent = it.getComponent<PhysicsEntityComponent>()
			val acceleration = it.getComponent<AccelerationComponent>().value

			physicsEntityComponent.body.applyForce(
				Double2(
					horizontalAcceleration * acceleration.x,
					verticalAcceleration * acceleration.y
				)
			)
		}
	}


	override val requirements = ECInclusionNode(PlayerComponent::class)
		.andInclude(AccelerationComponent::class)
		.andInclude(PhysicsEntityComponent::class)
		.andInclude(PhysicsDynamicEntityComponent::class)
		.andInclude(EnergyComponent::class)
}


class UserTouchMoveSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(
		PlayerComponent::class
	)
		.andInclude(PhysicsEntityComponent::class).andInclude(PhysicsDynamicEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		if (Input.hasSwiped) {
			val swipe = Input.swipeData
			val radians = swipe.currentDirection * kotlin.math.PI / 180.0
			val directionVector = Double2(kotlin.math.cos(radians), kotlin.math.sin(radians))
			directionVector.y = -directionVector.y
			val velocityVector = directionVector * swipe.velocity

			entities.forEach {
				val physics = it.getComponent<PhysicsEntityComponent>()
				val velocity = Double2()
				velocity.x = 3.0 * velocityVector.x + physics.body.velocity.x
				velocity.y = 10.0 * velocityVector.y + physics.body.velocity.y
				velocity.coerceAtMost(10.0)


				physics.body.velocity = velocity
			}
		}
	}

}