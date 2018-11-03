package game.modifiers

import kotlin.reflect.KClass

object ModifierManager {
	private val modifierLogics = mutableMapOf<KClass<IModifierLogic>, IModifierLogic>()
}