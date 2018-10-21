package game.modifiers

import engine.entity.Entity
import engine.physics.BodyBuilder

//abstract factory
//so modifiers can be recreated as many time as needed with separate internal states
interface IModifierFactory {
	fun build(sourceEntity: Entity): IModifier
}

abstract class TimeFactory<T> : IModifierFactory where T : IModifierFactory {
	protected var timeLeft: Double = 0.0

	fun setTimeLeft(timeLeft: Double) {
		this.timeLeft = timeLeft
	}

	protected open fun checkRequired() {
		if(timeLeft <= 0.0)
			throw Error("Time left must be set and larger than zero")
	}
}

class ShapeModifierFactory : TimeFactory<ShapeModifierFactory>() {
	private var bodyBuilder: BodyBuilder? = null

	fun setBodyBuilder(bodyBuilder: BodyBuilder) {
		this.bodyBuilder = bodyBuilder
	}

	override fun checkRequired() {
		super.checkRequired()
		if (bodyBuilder == null)
			throw NullPointerException("Body builder must be set before building")
	}

	override fun build(sourceEntity: Entity): IModifier {
		checkRequired()
		return ShapeModifier(sourceEntity, bodyBuilder!!, timeLeft)
	}
}