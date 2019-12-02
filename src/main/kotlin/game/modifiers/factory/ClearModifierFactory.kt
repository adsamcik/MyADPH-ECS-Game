package game.modifiers.factory

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.ClearModifierData
import game.modifiers.factory.template.IModifierFactory
import kotlinx.serialization.Serializable

@Serializable
class ClearModifierFactory : IModifierFactory {
	override fun build(sourceEntity: Entity): IModifierData {
		return ClearModifierData(sourceEntity, IModifierData.State.Active)
	}
}