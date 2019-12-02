package game.modifiers.logic.template

import engine.entity.Entity
import game.modifiers.data.template.IModifierData

abstract class PersistentModifierLogic<T : IModifierData>(entity: Entity) : BaseModifierLogic<T>(entity) {
	protected var currentModifier: T? = null
		private set

	private val _modifiers = mutableListOf<T>()

	override val modifiers: Collection<IModifierData>
		get() = _modifiers

	override val hasNoModifiers: Boolean
		get() = _modifiers.isEmpty()

	override fun setModifier(modifier: T) {
		if (hasNoModifiers) {
			save()
		}

		val currentModifier = currentModifier
		if (currentModifier?.entity == modifier.entity) {
			updateExistingModifier(currentModifier, modifier)
			return
		}

		val found = find { it.entity == modifier.entity }

		if (found != null) {
			updateExistingModifier(found, modifier)
		} else {
			add(modifier)
		}

		applyModifierInternal(modifier)
	}

	private fun applyModifierInternal(modifier: T) {
		this.currentModifier = modifier
		applyModifier(modifier)
	}


	override fun removeModifier(modifierData: IModifierData) {
		removeModifier(modifierData.unsafeCast<T>())

	}

	private fun removeModifier(modifierData: T) {
		_modifiers.remove(modifierData)
		if (modifierData == currentModifier) {
			onCurrentRemoved()
		}
	}

	override fun removeAllModifiers() {
		_modifiers.clear()
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

	protected open fun updateExistingModifier(currentModifier: T, newModifier: T) {
		currentModifier.state = newModifier.state
	}

	//Collection functions
	protected fun forEach(func: (item: T) -> Unit) {
		_modifiers.forEach(func)
	}

	protected fun find(func: (item: T) -> Boolean): T? {
		return _modifiers.find(func)
	}

	protected fun add(item: T) {
		_modifiers.add(item)
	}
}