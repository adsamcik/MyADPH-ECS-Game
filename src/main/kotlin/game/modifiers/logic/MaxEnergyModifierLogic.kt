package game.modifiers.logic

import ecs.components.EnergyComponent
import engine.entity.Entity
import game.modifiers.logic.template.TimeModifierLogic
import game.modifiers.data.MaxEnergyModifierData

class MaxEnergyModifierLogic(entity: Entity) : TimeModifierLogic<MaxEnergyModifierData>(entity) {
	private var defaultMaxEnergy: Double = 0.0
	private val energyComponent = entity.getComponent<EnergyComponent>()

	override fun applyModifier(modifier: MaxEnergyModifierData) {
		energyComponent.maxEnergy = modifier.maxEnergy
	}

	override fun restoreDefault() {
		energyComponent.maxEnergy = defaultMaxEnergy
	}

	override fun save() {
		defaultMaxEnergy = energyComponent.maxEnergy
	}

}