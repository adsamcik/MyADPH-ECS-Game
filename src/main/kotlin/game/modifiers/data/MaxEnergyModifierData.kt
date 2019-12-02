package game.modifiers.data

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.template.ITimeModifierData
import game.modifiers.logic.MaxEnergyModifierLogic

data class MaxEnergyModifierData(
	override val entity: Entity,
	override var state: IModifierData.State = IModifierData.State.Active,
	override var timeLeft: Double,
	val maxEnergy: Double
) : ITimeModifierData {
	override fun createNewLogicFor(entity: Entity) = MaxEnergyModifierLogic(entity)
}
