package game.modifiers.data

import engine.entity.Entity
import engine.physics.bodies.shapes.IShape
import game.modifiers.data.template.IModifierData
import game.modifiers.logic.template.IModifierLogic
import game.modifiers.data.template.ITimeModifierData
import game.modifiers.logic.ShapeModifierLogic

data class ShapeModifierData(
	override val entity: Entity,
	override var timeLeft: Double,
	override var state: IModifierData.State = IModifierData.State.Active,
	val shape: IShape
) : ITimeModifierData {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = ShapeModifierLogic(entity)
}