package game.modifiers

import engine.physics.BodyBuilder

interface IModifierFactory {
	fun build(): IModifier
}

abstract class TimeFactory<T> : IModifierFactory where T : IModifierFactory {
	protected var timeLeft: Double = 0.0

	fun setTimeLeft(timeLeft: Double): T {
		this.timeLeft = timeLeft
		@Suppress("UNCHECKED_CAST")
		return this as T
	}
}

class ShapeModifierFactory : TimeFactory<ShapeModifierFactory>() {

	private var bodyBuilder: BodyBuilder? = null

	fun setBodyBuilder(bodyBuilder: BodyBuilder): ShapeModifierFactory {
		this.bodyBuilder = bodyBuilder
		return this
	}

	override fun build(): IModifier {
		if (bodyBuilder == null)
			throw NullPointerException("Body builder must be set before building")

		return ShapeModifier(bodyBuilder!!, timeLeft)
	}
}