package game.modifiers.factory

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.AccelerationModifierData
import game.modifiers.factory.template.TimeModifierFactory
import general.Double2
import kotlinx.serialization.Serializable

@Serializable
class AccelerationModifierFactory : TimeModifierFactory() {
	var acceleration: Double2 = Double2()

	override fun build(sourceEntity: Entity): IModifierData {
		return AccelerationModifierData(sourceEntity,  IModifierData.State.Active, timeLeft, acceleration)
	}
}