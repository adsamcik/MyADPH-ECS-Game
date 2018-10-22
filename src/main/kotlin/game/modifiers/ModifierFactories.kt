package game.modifiers

import engine.entity.Entity
import engine.physics.BodyBuilder
import kotlinx.serialization.*

//abstract factory
//so modifiers can be recreated as many time as needed with separate internal states
@Serializable
interface IModifierFactory {
	fun build(sourceEntity: Entity): IModifier
}

@Serializer(forClass = IModifierFactory::class)
class ModifierSerializer : KSerializer<IModifierFactory> {
	override fun deserialize(input: Decoder): IModifierFactory {

	}

	override val descriptor: SerialDescriptor
		get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

	override fun serialize(output: Encoder, obj: IModifierFactory) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

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

@Serializable
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