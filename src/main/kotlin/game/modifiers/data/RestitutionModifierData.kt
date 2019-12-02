package game.modifiers.data

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.logic.template.IModifierLogic
import game.modifiers.data.template.ITimeModifierData
import game.modifiers.logic.RestitutionModifierLogic

data class RestitutionModifierData(
	override val entity: Entity,
	override var state: IModifierData.State = IModifierData.State.Active,
	override var timeLeft: Double,
	val restitution: Double
) : ITimeModifierData {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = RestitutionModifierLogic(entity)
}
