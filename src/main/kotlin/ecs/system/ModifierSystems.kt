package ecs.system

import ecs.components.modifiers.ActiveModifierComponent
import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import game.modifiers.IModifier
import game.modifiers.IModifierLogic
import utility.ECInclusionNode
import utility.INode
import kotlin.reflect.KClass

class ModifierUpdateSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(ModifierReceiverComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val modifierComponent = entity.getComponent(ModifierReceiverComponent::class)
			modifierComponent.modifierLogics.forEach { (type, logic) ->
				logic.update(deltaTime)
				checkModifiers(entity, type, logic, modifierComponent)
			}
		}
	}

	private fun checkModifiers(
		entity: Entity,
		type: KClass<out IModifier>,
		logic: IModifierLogic,
		modifierReceiverComponent: ModifierReceiverComponent
	) {
		if (logic.hasNoModifiers) {
			modifierReceiverComponent.modifierLogics.remove(type)

			if (modifierReceiverComponent.modifierLogics.isEmpty())
				EntityManager.removeComponent(entity, ActiveModifierComponent::class)
		}
	}
}