package ecs.system

import ecs.components.AccelerationComponent
import ecs.components.EnergyComponent
import ecs.components.PlayerComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Core
import engine.entity.Entity
import engine.graphics.Graphics
import engine.input.Input
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import engine.system.requirements.andExclude
import engine.system.requirements.andInclude
import extensions.radiansToVector
import extensions.toRadians
import general.Double2
import general.Int2


class DevMoveSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		if (horizontalInput == 0.0 && verticalInput == 0.0) return

		val horizontalAcceleration = horizontalInput * deltaTime * 2
		val verticalAcceleration = verticalInput * deltaTime * 6.8


		entities.forEach {
			val physicsEntityComponent = it.getComponent<PhysicsEntityComponent>()
			val body = physicsEntityComponent.body

			val velocity = body.velocity

			val mass = body.mass

			velocity.x += horizontalAcceleration * mass
			velocity.y += verticalAcceleration * mass

			body.applyForce(Double2(horizontalAcceleration, verticalAcceleration))
			body.position = Double2()
		}
	}


	override val requirements = ECInclusionNode(PlayerComponent::class)
		.andInclude(PhysicsEntityComponent::class)
		.andInclude(PhysicsDynamicEntityComponent::class)
		.andExclude(EnergyComponent::class)
}

class KeyboardMoveSystem : BaseMoveSystem() {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val horizontalInput = Input.horizontal()
		val verticalInput = Input.vertical()

		move(entities, deltaTime, Double2(horizontalInput, verticalInput))
	}


	override val requirements = ECInclusionNode(PlayerComponent::class)
		.andInclude(AccelerationComponent::class)
		.andInclude(PhysicsEntityComponent::class)
		.andInclude(PhysicsDynamicEntityComponent::class)
		.andInclude(EnergyComponent::class)
}


class UserTouchMoveSystem : BaseMoveSystem() {
	override val requirements: INode<Entity> = ECInclusionNode(PlayerComponent::class)
		.andInclude(PhysicsEntityComponent::class)
		.andInclude(PhysicsDynamicEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val touches = Input.touchList

		if (touches.isNotEmpty()) {
			val centerX = Graphics.screenCenter
			val directionVector = touches[0].position - centerX

			move(entities, deltaTime, directionVector.toDouble2())
		}
	}

}

abstract class BaseMoveSystem : ISystem {
	fun move(entities: Collection<Entity>, deltaTime: Double, input: Double2) {
		if (input.x == 0.0 && input.y == 0.0) return

		val normalizedInput = input.normalized
		val horizontalAcceleration = normalizedInput.x * deltaTime
		var verticalAcceleration = normalizedInput.y * deltaTime

		val deltaForce = (deltaTime * -normalizedInput.y).coerceAtLeast(0.0)

		entities.forEach {
			if (deltaForce > 0) {
				val energyComponent = it.getComponent<EnergyComponent>()

				val deltaDraw = deltaForce * energyComponent.currentDraw * energyComponent.maxEnergyUsage
				if (energyComponent.energy < deltaDraw) {
					verticalAcceleration = 0.0
				} else {
					energyComponent.currentDraw = (energyComponent.currentDraw + deltaTime).coerceAtMost(1.0)
					energyComponent.energy -= deltaDraw
					energyComponent.lastUseTime = Core.time
				}
			}

			val physicsEntityComponent = it.getComponent<PhysicsEntityComponent>()
			val acceleration = it.getComponent<AccelerationComponent>().value
			val body = physicsEntityComponent.body
			val mass = body.mass

			body.applyForce(
				Double2(
					horizontalAcceleration * acceleration.x * mass,
					verticalAcceleration * acceleration.y * mass
				)
			)
		}
	}
}