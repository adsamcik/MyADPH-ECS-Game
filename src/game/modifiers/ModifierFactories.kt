package game.modifiers

import engine.entity.Entity
import engine.physics.BodyBuilder

//abstract factory
//so modifiers can be recreated as many time as needed with separate internal states
interface IModifierFactory {
	fun build(): IModifier
}

abstract class TimeFactory<T> : IModifierFactory where T : IModifierFactory {
	protected var timeLeft: Double = 0.0
	protected var entity: Entity? = null

	fun setTimeLeft(timeLeft: Double): T {
		this.timeLeft = timeLeft

		return this.unsafeCast<T>()
	}

	fun setEntity(entity: Entity): T {
		this.entity = entity

		return this.unsafeCast<T>()
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

	fun setBodyBuilder(bodyBuilder: BodyBuilder): ShapeModifierFactory {
		this.bodyBuilder = bodyBuilder
		return this
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