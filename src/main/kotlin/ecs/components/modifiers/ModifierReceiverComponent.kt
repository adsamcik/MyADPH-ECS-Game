package ecs.components.modifiers

import engine.component.IComponent
import game.modifiers.IModifier
import game.modifiers.IModifierLogic
import kotlin.reflect.KClass

class ModifierReceiverComponent : IComponent {
	val modifierLogics = mutableMapOf<KClass<out IModifier>, IModifierLogic>()
}