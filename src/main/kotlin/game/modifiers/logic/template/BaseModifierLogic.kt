package game.modifiers.logic.template

import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.data.template.ITimeModifierData


abstract class BaseModifierLogic<T : IModifierData>(protected val entity: Entity) :
	IModifierLogic {
	override val hasNoModifiers: Boolean
		get() = true

	override fun setModifier(modifierData: IModifierData) {
		//checking if it's safe is actually pretty difficult with generics
		setModifier(modifierData.unsafeCast<T>())
	}

	open fun setModifier(modifier: T) = applyModifier(modifier)

	protected abstract fun applyModifier(modifier: T)
	protected abstract fun restoreDefault()
	protected abstract fun save()

	override fun update(deltaTime: Double) {
		internalUpdate(deltaTime)
	}

	protected open fun internalUpdate(deltaTime: Double) = Unit
}