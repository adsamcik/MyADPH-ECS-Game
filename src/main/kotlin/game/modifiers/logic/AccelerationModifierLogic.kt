package game.modifiers.logic

import ecs.components.AccelerationComponent
import engine.entity.Entity
import game.modifiers.logic.template.TimeModifierLogic
import game.modifiers.data.AccelerationModifierData
import general.Double2

class AccelerationModifierLogic(entity: Entity) : TimeModifierLogic<AccelerationModifierData>(entity) {
	private var defaultAcceleration: Double2 = Double2()
	private val accelerationComponent = entity.getComponent<AccelerationComponent>()

	override fun applyModifier(modifier: AccelerationModifierData) {
		accelerationComponent.value = modifier.acceleration
	}

	override fun restoreDefault() {
		accelerationComponent.value = defaultAcceleration
	}

	override fun save() {
		defaultAcceleration = accelerationComponent.value
	}

}