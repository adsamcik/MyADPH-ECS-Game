package ecs.system

import ecs.components.HealthComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.triggers.CheckpointMemoryComponent
import engine.entity.Entity
import engine.graphics.UserInterface
import engine.system.ISystem
import utility.Double2
import utility.ECInclusionNode
import utility.andInclude

class HealthUpdateSystem : ISystem {
	override val requirements = ECInclusionNode(HealthComponent::class)
		.andInclude(CheckpointMemoryComponent::class)
		.andInclude(PhysicsEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val healthComponent = entity.getComponent<HealthComponent>()

			if (healthComponent.damagers.isNotEmpty()) {
				healthComponent.damagers.forEach { healthComponent.health -= it.value * deltaTime }

				if (healthComponent.health <= 0) {
					val body = entity.getComponent<PhysicsEntityComponent>().body
					val lastCheckpoint = entity.getComponent<CheckpointMemoryComponent>().lastCheckpoint
					body.position = lastCheckpoint.respawnPosition
					body.velocity = Double2()
					body.wakeup()

					healthComponent.health = healthComponent.maxHealth
					healthComponent.damagers.clear()
				}

				UserInterface.updateHealth(healthComponent.health, healthComponent.maxHealth)
			}
		}
	}

}