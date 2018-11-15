package game.modifiers

import engine.entity.Entity
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.serialization.GenericSerializer
import game.modifiers.data.MaxEnergyModifierData
import game.modifiers.data.ShapeModifierData
import kotlinx.serialization.*

//abstract factory
//so modifierLogic can be recreated as many time as needed with separate internal states
@Serializable(with = ModifierSerializer::class)
interface IModifierFactory {
	fun build(sourceEntity: Entity): IModifierData
}

@Serializer(forClass = IModifierFactory::class)
object ModifierSerializer : GenericSerializer<IModifierFactory>("modifier") {
	override fun deserialize(type: String, structure: CompositeDecoder) = when (type) {
		ShapeModifierFactory::class.simpleName -> structure.decodeSerializableElement(
			descriptor,
			StructureDescriptor.DATA_INDEX,
			ShapeModifierFactory.serializer()
		)
		else -> throw NotImplementedError("Deserialization for $type not implemented")
	}

	override fun serialize(output: Encoder, obj: IModifierFactory) {
		when (obj) {
			is ShapeModifierFactory -> serialize(output, obj, ShapeModifierFactory.serializer())
			else -> throw Error("Serializer for ${obj::class.simpleName} not implemented")
		}
	}

	override val descriptor: SerialDescriptor
		get() = super.descriptor

	override fun deserialize(input: Decoder): IModifierFactory {
		return super.deserialize(input)
	}
}

abstract class TimeFactory: IModifierFactory {
	protected var timeLeft: Double = 0.0

	fun setTimeLeft(timeLeft: Double) {
		this.timeLeft = timeLeft
	}

	protected open fun validateBuilder() {
		if (timeLeft <= 0.0)
			throw Error("Time left must be set and larger than zero")
	}
}

@Serializable
class ShapeModifierFactory : TimeFactory() {
	var bodyBuilder: MutableBodyBuilder? = null

	override fun validateBuilder() {
		super.validateBuilder()
		if (bodyBuilder == null)
			throw NullPointerException("Body builder must be set before building")
	}

	override fun build(sourceEntity: Entity): IModifierData {
		validateBuilder()
		return ShapeModifierData(sourceEntity, timeLeft, shape = bodyBuilder!!.shape)
	}
}

class MaxEnergyModifierFactory : TimeFactory() {
	var maxEnergy: Double = 0.0

	override fun build(sourceEntity: Entity): IModifierData {
		return MaxEnergyModifierData(sourceEntity, timeLeft, IModifierData.State.Active, maxEnergy)
	}
}