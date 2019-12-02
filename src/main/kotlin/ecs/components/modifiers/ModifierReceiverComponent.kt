package ecs.components.modifiers

import engine.component.IComponent
import engine.entity.Entity
import game.modifiers.data.template.IModifierData
import game.modifiers.logic.template.IModifierLogic
import kotlin.reflect.KClass

class ModifierReceiverComponent(val entity: Entity) : IComponent {
	val modifierLogicList = mutableMapOf<KClass<out IModifierData>, IModifierLogic>()
}