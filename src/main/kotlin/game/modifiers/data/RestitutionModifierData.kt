package game.modifiers.data

import engine.entity.Entity
import game.modifiers.IModifierData
import game.modifiers.IModifierLogic
import game.modifiers.logic.RestitutionModifierLogic

data class RestitutionModifierData(
	override val entity: Entity,
	override var timeLeft: Double,
	override var state: IModifierData.State = IModifierData.State.Active,
	val restitution: Double
) : IModifierData {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = RestitutionModifierLogic(entity)
}
