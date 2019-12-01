package game.modifiers.data

import engine.entity.Entity
import game.modifiers.IModifierData
import game.modifiers.logic.MaxEnergyModifierLogic

data class MaxHealthModifierData(
	override val entity: Entity,
	override var state: IModifierData.State = IModifierData.State.Active,
	override var timeLeft: Double,
	val maxHealth: Double
) : IModifierData {
	override fun createNewLogicFor(entity: Entity) = MaxEnergyModifierLogic(entity)
}
