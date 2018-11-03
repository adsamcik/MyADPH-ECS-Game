package game.modifiers

import engine.entity.Entity

interface IModifierLogic {
	val modifiers: Collection<IModifier>
	val hasNoModifiers: Boolean

	fun update(deltaTime: Double)

	fun setModifier(modifier: IModifier)
	fun removeModifier(modifier: IModifier)
}

abstract class ModifierLogic<T : IModifier> : IModifierLogic {
	private val _modifiers = mutableListOf<T>()

	override val modifiers: Collection<IModifier>
		get() = _modifiers

	override val hasNoModifiers: Boolean
		get() = _modifiers.isEmpty()

	override fun setModifier(modifier: IModifier) {
		//checking if it's safe is actually pretty difficult with generics
		setModifier(modifier.unsafeCast<T>())
	}

	override fun removeModifier(modifier: IModifier) {
		removeModifier(modifier.unsafeCast<T>())
	}

	fun setModifier(modifier: T) {
		val found = _modifiers.find { it.entity == modifier.entity }

		if (found != null) {
			found.timeLeft = modifier.timeLeft
			found.state = modifier.state
		} else
			_modifiers.add(modifier)
	}

	fun removeModifier(modifier: T) {
		_modifiers.remove(modifier)
	}

	fun removeModifiersFrom(entity: Entity) {
		_modifiers.removeAll { it.entity == entity }
	}

	override fun update(deltaTime: Double) {
		_modifiers.forEach {
			it.timeLeft -= deltaTime
		}

		internalUpdate(deltaTime)
	}

	protected open fun internalUpdate(deltaTime: Double) {}
}

class ShapeModifierLogic : ModifierLogic<ShapeModifier>()

class RestitutionModifierLogic : ModifierLogic<RestitutionModifier>()