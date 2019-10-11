package ecs.system

import ecs.components.EnergyComponent
import ecs.components.PlayerComponent
import ecs.components.health.HealthComponent
import engine.entity.Entity
import engine.graphics.ui.UserInterface
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import engine.system.requirements.andInclude

class UpdateUserInterfaceSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(HealthComponent::class)
			.andInclude(EnergyComponent::class)
			.andInclude(PlayerComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val entity = entities.first()

		val healthComponent = entity.getComponent<HealthComponent>()

		UserInterface.updateHealth(healthComponent.health, healthComponent.maxHealth)

		val energyComponent = entity.getComponent<EnergyComponent>()
		UserInterface.updateEnergy(energyComponent.energy, energyComponent.maxEnergy)
	}
}