package game.modifiers

import engine.entity.Entity
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.serialization.GenericSerializer
import game.modifiers.data.MaxEnergyModifierData
import game.modifiers.data.ShapeModifierData
import kotlinx.serialization.*

//PATTERN Abstract Factory
//so modifierLogic can be recreated as many time as needed with separate internal states
interface IModifierFactory {
	fun build(sourceEntity: Entity): IModifierData
}

@Serializable
abstract class TimeFactory: IModifierFactory {
	var timeLeft: Double = 0.0

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

@Serializable
class MaxEnergyModifierFactory : TimeFactory() {
	var maxEnergy: Double = 0.0

	override fun build(sourceEntity: Entity): IModifierData {
		return MaxEnergyModifierData(sourceEntity, timeLeft, IModifierData.State.Active, maxEnergy)
	}
}