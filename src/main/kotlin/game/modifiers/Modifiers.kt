package game.modifiers

import engine.entity.Entity
import engine.physics.BodyBuilder

interface IModifier {
	val entity: Entity
	var state: IModifier.State
	var timeLeft: Double

	fun createNewLogicFor(entity: Entity): IModifierLogic

	enum class State {
		Active,
		ActiveTimePaused,
		Paused
	}
}

data class ShapeModifier(
	override val entity: Entity,
	override var timeLeft: Double,
	override var state: IModifier.State = IModifier.State.Active,
	val bodyBuilder: BodyBuilder
) : IModifier {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = ShapeModifierLogic(entity)
}

data class RestitutionModifier(
	override val entity: Entity,
	override var timeLeft: Double,
	override var state: IModifier.State = IModifier.State.Active,
	val restitution: Double
) : IModifier {
	override fun createNewLogicFor(entity: Entity): IModifierLogic = RestitutionModifierLogic(entity)
}
