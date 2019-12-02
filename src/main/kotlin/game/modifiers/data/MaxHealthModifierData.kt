package game.modifiers.data

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.template.ITimeModifierData
import game.modifiers.logic.MaxEnergyModifierLogic
import game.modifiers.logic.MaxHealthModifierLogic

data class MaxHealthModifierData(
	override val entity: Entity,
	override var state: IModifierData.State = IModifierData.State.Active,
	override var timeLeft: Double,
	val maxHealth: Double
) : ITimeModifierData {
	override fun createNewLogicFor(entity: Entity) = MaxHealthModifierLogic(entity)
}
