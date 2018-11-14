package game.modifiers

import engine.entity.Entity

interface IModifierLogic {
	val modifiers: Collection<IModifierData>
	val hasNoModifiers: Boolean

	fun update(deltaTime: Double)

	fun setModifier(modifierData: IModifierData)
	fun removeModifier(modifierData: IModifierData)
}

abstract class ModifierLogic<T : IModifierData>(protected val entity: Entity) : IModifierLogic {
	private var currentModifier: T? = null

	private val _modifiers = mutableListOf<T>()

	override val modifiers: Collection<IModifierData>
		get() = _modifiers

	override val hasNoModifiers: Boolean
		get() = _modifiers.isEmpty()

	override fun setModifier(modifierData: IModifierData) {
		//checking if it's safe is actually pretty difficult with generics
		setModifier(modifierData.unsafeCast<T>())
	}

	override fun removeModifier(modifierData: IModifierData) {
		removeModifier(modifierData.unsafeCast<T>())
	}

	fun setModifier(modifier: T) {
		if (_modifiers.isEmpty())
			save()


		val currentModifier = currentModifier
		if (currentModifier?.entity == modifier.entity) {
			currentModifier.timeLeft = modifier.timeLeft
			currentModifier.state = modifier.state
			return
		}

		val found = _modifiers.find { it.entity == modifier.entity }

		if (found != null) {
			found.timeLeft = modifier.timeLeft
			found.state = modifier.state
		} else
			_modifiers.add(modifier)

		applyModifierInternal(modifier)
	}

	private fun applyModifierInternal(modifier: T) {
		currentModifier = modifier
		applyModifier(modifier)
	}

	protected abstract fun applyModifier(modifier: T)
	protected abstract fun restoreDefault()
	protected abstract fun save()

	fun removeModifier(modifier: T) {
		_modifiers.remove(modifier)
		if (modifier == currentModifier)
			onCurrentRemoved()
	}

	private fun onCurrentRemoved() {
		if (_modifiers.isNotEmpty()) {
			val last = _modifiers.last()
			applyModifier(last)
		} else {
			currentModifier = null
			restoreDefault()
		}
	}

	fun removeModifiers(predicate: (T) -> Boolean) {
		if (_modifiers.removeAll(predicate) && !_modifiers.contains(currentModifier)) {
			onCurrentRemoved()
		}
	}

	override fun update(deltaTime: Double) {
		_modifiers.forEach { it.timeLeft -= deltaTime }

		removeModifiers { it.timeLeft <= 0 }

		internalUpdate(deltaTime)
	}

	protected open fun internalUpdate(deltaTime: Double) {}
}