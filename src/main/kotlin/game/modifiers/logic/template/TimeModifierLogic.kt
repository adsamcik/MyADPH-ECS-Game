package game.modifiers.logic.template

import engine.entity.Entity
import game.modifiers.data.template.ITimeModifierData

abstract class TimeModifierLogic<T : ITimeModifierData>(entity: Entity) : PersistentModifierLogic<T>(entity) {
	override fun updateExistingModifier(currentModifier: T, newModifier: T) {
		super.updateExistingModifier(currentModifier, newModifier)
		currentModifier.timeLeft = newModifier.timeLeft
	}

	override fun internalUpdate(deltaTime: Double) {
		forEach { it.timeLeft -= deltaTime }

		removeModifiers { it.timeLeft <= 0 }
	}
}