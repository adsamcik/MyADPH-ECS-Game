package game.modifiers.data

import engine.entity.Entity
import game.modifiers.IModifierData
import game.modifiers.logic.AccelerationModifierLogic
import general.Double2

data class AccelerationModifierData(
	override val entity: Entity,
	override var state: IModifierData.State,
	override var timeLeft: Double,
	val acceleration: Double2
) : IModifierData {
	override fun createNewLogicFor(entity: Entity): game.modifiers.IModifierLogic =
		AccelerationModifierLogic(entity)
}
