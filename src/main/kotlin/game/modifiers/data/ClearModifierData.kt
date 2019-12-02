package game.modifiers.data

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.logic.ClearModifierLogic
import game.modifiers.logic.template.IModifierLogic

data class ClearModifierData(
	override val entity: Entity,
	override var state: IModifierData.State
) : IModifierData {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = ClearModifierLogic(entity)
}