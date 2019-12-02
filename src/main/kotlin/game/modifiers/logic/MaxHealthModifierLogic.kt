package game.modifiers.logic

import ecs.components.health.HealthComponent
import engine.entity.Entity
import game.modifiers.logic.template.TimeModifierLogic
import game.modifiers.data.MaxHealthModifierData

class MaxHealthModifierLogic(entity: Entity) : TimeModifierLogic<MaxHealthModifierData>(entity) {
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