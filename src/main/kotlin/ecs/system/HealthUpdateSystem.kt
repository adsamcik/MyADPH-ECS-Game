package ecs.system

import ecs.components.EnergyComponent
import ecs.components.health.HealthComponent
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.triggers.CheckpointMemoryComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.andInclude
import general.Double2

class HealthUpdateSystem : ISystem {
	override val requirements = ECInclusionNode(HealthComponent::class)
		.andInclude(PhysicsEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val healthComponent = entity.getComponent<HealthComponent>()

			healthComponent.damagers.forEach { healthComponent.health -= it.value * deltaTime }

			if (healthComponent.health <= 0) {
				if (EntityManager.hasComponent(entity, CheckpointMemoryComponent::class)) {
					respawn(entity, healthComponent)
				} else {
					EntityManager.removeEntity(entity)
				}
			}
		}
	}

	private fun respawn(entity: Entity, healthComponent: HealthComponent) {
		val body = entity.getComponent<PhysicsEntityComponent>().body
		val lastCheckpoint = entity.getComponent<CheckpointMemoryComponent>().lastCheckpoint
		body.position = lastCheckpoint.respawnPosition
		body.velocity = Double2()
		body.wakeup()

		healthComponent.health = healthComponent.maxHealth
		healthComponent.damagers.clear()

		val energyComponent = entity.getComponent<EnergyComponent>()
		energyComponent.currentDraw = 0.0
		energyComponent.energy = energyComponent.maxEnergy
		energyComponent.lastUseTime = 0.0

		val modifiers = entity.getComponent<ModifierReceiverComponent>()
		modifiers.modifierLogicList.forEach { it.value.removeAllModifiers() }
	}

}