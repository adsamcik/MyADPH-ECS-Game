package game.modifiers.data.template

import engine.entity.Entity
import game.modifiers.logic.template.IModifierLogic

interface IModifierData {
	val entity: Entity
	var state: State

	fun createNewLogicFor(entity: Entity): IModifierLogic

	enum class State {
		Active,
		ActiveTimePaused,
		Paused
	}
}