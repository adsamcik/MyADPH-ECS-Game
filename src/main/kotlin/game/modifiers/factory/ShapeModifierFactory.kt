package game.modifiers.factory

import engine.entity.Entity
import engine.physics.bodies.builder.MutableBodyBuilder
import game.modifiers.data.template.IModifierData
import game.modifiers.data.ShapeModifierData
import game.modifiers.factory.template.TimeModifierFactory
import kotlinx.serialization.Serializable

@Serializable
class ShapeModifierFactory : TimeModifierFactory() {
	var bodyBuilder: MutableBodyBuilder? = null

	override fun validateBuilder() {
		super.validateBuilder()
		requireNotNull(bodyBuilder) { "Body builder must be set before building" }
	}

	override fun build(sourceEntity: Entity): IModifierData {
		validateBuilder()
		return ShapeModifierData(sourceEntity, timeLeft, shape = requireNotNull(bodyBuilder).shape)
	}
}