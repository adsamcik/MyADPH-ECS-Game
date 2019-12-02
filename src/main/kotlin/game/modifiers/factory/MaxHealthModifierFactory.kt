package game.modifiers.factory

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.MaxHealthModifierData
import game.modifiers.factory.template.TimeModifierFactory
import kotlinx.serialization.Serializable

@Serializable
class MaxHealthModifierFactory : TimeModifierFactory() {
	var maxHealth: Double = 0.0

	override fun build(sourceEntity: Entity): IModifierData {
		return MaxHealthModifierData(sourceEntity, IModifierData.State.Active, timeLeft, maxHealth)
	}
}
