package game.modifiers.logic.template

import game.modifiers.data.template.IModifierData

interface IModifierLogic {
	val modifiers: Collection<IModifierData>
	val hasNoModifiers: Boolean

	fun update(deltaTime: Double)

	fun setModifier(modifierData: IModifierData)
	fun removeModifier(modifierData: IModifierData)
	fun removeAllModifiers()
}