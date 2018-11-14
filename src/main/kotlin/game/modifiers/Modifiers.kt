package game.modifiers

import engine.entity.Entity

interface IModifierData {
	val entity: Entity
	var state: IModifierData.State
	var timeLeft: Double

	fun createNewLogicFor(entity: Entity): IModifierLogic

	enum class State {
		Active,
		ActiveTimePaused,
		Paused
	}
}