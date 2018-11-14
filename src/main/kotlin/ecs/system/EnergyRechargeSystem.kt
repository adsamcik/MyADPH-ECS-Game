package ecs.system

import ecs.components.EnergyComponent
import engine.Core
import engine.entity.Entity
import engine.graphics.UserInterface
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode

class EnergyRechargeSystem : ISystem {

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val energyComponent = it.getComponent<EnergyComponent>()
			UserInterface.updateEnergy(energyComponent.energy, energyComponent.maxEnergy)
			if (Core.time - (energyComponent.lastUseTime + deltaTime) < 0.3)
				return@forEach

			energyComponent.energy =
					(energyComponent.energy + energyComponent.rechargeSpeed * deltaTime).coerceAtMost(energyComponent.maxEnergy)
		}
	}

	override val requirements: INode<Entity> = ECInclusionNode(EnergyComponent::class)

}