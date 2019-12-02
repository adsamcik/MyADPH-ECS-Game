package game.modifiers.factory.template

import kotlinx.serialization.Serializable


@Serializable
abstract class TimeModifierFactory : IModifierFactory {
	var timeLeft: Double = 0.0

	protected open fun validateBuilder() {
		if (timeLeft <= 0.0) {
			throw Error("Time left must be set and larger than zero")
		}
	}
}
