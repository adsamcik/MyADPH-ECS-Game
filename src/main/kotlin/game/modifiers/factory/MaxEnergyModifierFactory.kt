package game.modifiers.factory

import engine.entity.Entity
import game.modifiers.IModifierData
import game.modifiers.data.MaxEnergyModifierData
import game.modifiers.factory.template.TimeFactory
import kotlinx.serialization.Serializable

@Serializable
class MaxEnergyModifierFactory : TimeFactory() {
	var maxEnergy: Double = 0.0

	override fun build(sourceEntity: Entity): IModifierData {
		return MaxEnergyModifierData(sourceEntity, IModifierData.State.Active, timeLeft, maxEnergy)
	}
}
