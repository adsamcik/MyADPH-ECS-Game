package game.modifiers

import engine.entity.Entity
import engine.physics.BodyBuilder

//abstract factory
//so modifiers can be recreated as many time as needed with separate internal states
interface IModifierFactory {
	fun setEntity(entity: Entity)

	fun build(): IModifier
}

abstract class TimeFactory<T> : IModifierFactory where T : IModifierFactory {
	protected var timeLeft: Double = 0.0
	var entity: Entity? = null

	fun setTimeLeft(timeLeft: Double) {
		this.timeLeft = timeLeft
	}

	override fun setEntity(entity: Entity) {
		this.entity = entity
	}

	protected open fun checkRequired() {
		if(timeLeft <= 0.0)
			throw Error("Time left must be set and larger than zero")

		if(entity == null)
			throw Error("Entity must be set")
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

	override fun build(): IModifier {
		checkRequired()
		return ShapeModifier(entity!!, bodyBuilder!!, timeLeft)
	}
}