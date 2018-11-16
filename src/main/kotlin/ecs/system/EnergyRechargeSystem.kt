package ecs.system

import ecs.components.EnergyComponent
import engine.Core
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode

class EnergyRechargeSystem : ISystem {

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val energyComponent = it.getComponent<EnergyComponent>()

			if (Core.time - (energyComponent.lastUseTime + deltaTime) < 0.3)
				return@forEach
			else if (energyComponent.energy == energyComponent.maxEnergy) {
				energyComponent.currentDraw = 0.0
				return@forEach
			}

			energyComponent.energy =
					(energyComponent.energy + energyComponent.rechargeSpeed * deltaTime).coerceAtMost(energyComponent.maxEnergy)
		}
	}

	override val requirements: INode<Entity> = ECInclusionNode(EnergyComponent::class)

}