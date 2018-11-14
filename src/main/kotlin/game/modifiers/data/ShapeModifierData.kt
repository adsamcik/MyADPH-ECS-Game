package game.modifiers.data

import engine.entity.Entity
import engine.physics.IShape
import game.modifiers.IModifierData
import game.modifiers.IModifierLogic
import game.modifiers.logic.ShapeModifierLogic

data class ShapeModifierData(
	override val entity: Entity,
	override var timeLeft: Double,
	override var state: IModifierData.State = IModifierData.State.Active,
	val shape: IShape
) : IModifierData {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = ShapeModifierLogic(entity)
}