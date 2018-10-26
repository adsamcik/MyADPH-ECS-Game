package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import engine.physics.BodyBuilder

interface IModifier {
	val id: String

	fun apply(modifierComponent: ModifierReceiverComponent)
	fun restore(modifierComponent: ModifierReceiverComponent)
}

interface IPhysicsModifier : IModifier

interface ITimedModifier : IModifier {
	val timeLeft: Double
	val hasTimeLeft: Boolean

	fun update(deltaTime: Double)
}

abstract class TimedModifier(entity: Entity, private var _timeLeft: Double) : ITimedModifier {
	override val id: String = "$entity${this::class.simpleName}"

	override val timeLeft: Double
		get() = _timeLeft

	override fun update(deltaTime: Double) {
		_timeLeft -= deltaTime
	}

	override val hasTimeLeft: Boolean
		get() = _timeLeft > 0
}


class ShapeModifier(entity: Entity, val shape: BodyBuilder, timeLeft: Double) : TimedModifier(entity, timeLeft), IPhysicsModifier {

	override fun restore(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.restoreBody()
	}

	override fun apply(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.setBody(shape)
	}

}

class BouncinessModifier(entity: Entity, val value: Double, timeLeft: Double) : TimedModifier(entity, timeLeft), IPhysicsModifier {
	override fun apply(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.setRestitution(value)
	}

	override fun restore(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.restoreRestitution()
	}
}

/*class DensityModifier(entity: Entity, val value: Double, timeLeft: Double) : TimedModifier(entity, timeLeft), IPhysicsModifier {
	override fun restore(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.restoreDensity()
	}

	override fun apply(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.setDensity(value)
	}
}*/