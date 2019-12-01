package game.modifiers.logic

import ecs.components.health.HealthComponent
import engine.entity.Entity
import game.modifiers.ModifierLogic
import game.modifiers.data.MaxEnergyModifierData
import game.modifiers.data.MaxHealthModifierData

class MaxHealthModifierLogic(entity: Entity) : ModifierLogic<MaxHealthModifierData>(entity) {
	private var defaultMaxHealth: Double = 0.0
	private val healthComponent = entity.getComponent<HealthComponent>()

	override fun applyModifier(modifier: MaxHealthModifierData) {
		healthComponent.maxHealth = modifier.maxHealth
	}

	override fun restoreDefault() {
		healthComponent.maxHealth = defaultMaxHealth
	}

	override fun save() {
		defaultMaxHealth = healthComponent.maxHealth
	}

}