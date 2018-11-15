package game.modifiers.data

import engine.entity.Entity
import game.modifiers.IModifierData
import game.modifiers.logic.MaxEnergyModifierLogic

data class MaxEnergyModifierData(
	override val entity: Entity,
	override var timeLeft: Double,
	override var state: IModifierData.State = IModifierData.State.Active,
	val maxEnergy: Double
	) : IModifierData {
	override fun createNewLogicFor(entity: Entity) = MaxEnergyModifierLogic(entity)
}
