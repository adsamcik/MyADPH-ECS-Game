package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.physics.BodyBuilder

interface IModifier {
	fun apply(modifierComponent: ModifierReceiverComponent)
	fun restore(modifierComponent: ModifierReceiverComponent)
}

interface IPhysicsModifier : IModifier

interface ITimedModifier : IModifier {
	val timeLeft: Double
	val hasTimeLeft: Boolean

	fun update(deltaTime: Double)
}

abstract class TimedModifier(private var _timeLeft: Double) : ITimedModifier {
	override val timeLeft: Double
		get() = _timeLeft

	override fun update(deltaTime: Double) {
		_timeLeft -= deltaTime
	}

	override val hasTimeLeft: Boolean
		get() = _timeLeft > 0
}


class ShapeModifier(val shape: BodyBuilder, timeLeft: Double) : TimedModifier(timeLeft), IPhysicsModifier {
	override fun restore(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.restoreBody()
	}

	override fun apply(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.setBody(shape)
	}

}

class BouncinessModifier(val value: Number, timeLeft: Double) : TimedModifier(timeLeft), IPhysicsModifier {
	override fun apply(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.setRestitution(value)
	}

	override fun restore(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.restoreRestitution()
	}
}

class DensityModifier(val value: Number, timeLeft: Double) : TimedModifier(timeLeft), IPhysicsModifier {
	override fun restore(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.restoreDensity()
	}

	override fun apply(modifierComponent: ModifierReceiverComponent) {
		modifierComponent.setDensity(value)
	}
}