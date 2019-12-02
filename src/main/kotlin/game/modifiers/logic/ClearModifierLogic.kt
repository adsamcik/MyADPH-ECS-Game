package game.modifiers.logic

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import game.modifiers.data.ClearModifierData
import game.modifiers.data.template.IModifierData
import game.modifiers.logic.template.BaseModifierLogic

class ClearModifierLogic(entity: Entity) : BaseModifierLogic<ClearModifierData>(entity) {
	override fun applyModifier(modifier: ClearModifierData) {
		val modifiers = entity.getComponent<ModifierReceiverComponent>().modifierLogicList
		modifiers.forEach {
			it.value.removeAllModifiers()
		}
		modifiers.clear()
	}

	override fun restoreDefault() = Unit

	override fun save() = Unit

	override val modifiers: Collection<IModifierData>
		get() = emptyList()

	override fun removeAllModifiers() = Unit

	override fun removeModifier(modifierData: IModifierData) = Unit
}