package ecs.components.modifiers

import engine.component.IComponent
import engine.entity.Entity
import game.modifiers.IModifierData
import game.modifiers.IModifierLogic
import kotlin.reflect.KClass

class ModifierReceiverComponent(val entity: Entity) : IComponent {
	val modifierLogicList = mutableMapOf<KClass<out IModifierData>, IModifierLogic>()
}