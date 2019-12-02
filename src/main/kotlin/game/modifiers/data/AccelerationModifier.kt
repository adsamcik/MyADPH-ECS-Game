package game.modifiers.data

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.template.ITimeModifierData
import game.modifiers.logic.AccelerationModifierLogic
import game.modifiers.logic.template.IModifierLogic
import general.Double2

data class AccelerationModifierData(
	override val entity: Entity,
	override var state: IModifierData.State,
	override var timeLeft: Double,
	val acceleration: Double2
) : ITimeModifierData {
	override fun createNewLogicFor(entity: Entity): IModifierLogic =
		AccelerationModifierLogic(entity)
}
