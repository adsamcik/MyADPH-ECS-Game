package ecs.system

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import engine.system.ISystem
import game.modifiers.data.template.IModifierData
import game.modifiers.logic.template.IModifierLogic
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import kotlin.reflect.KClass

class ModifierUpdateSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(ModifierReceiverComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val modifierComponent = entity.getComponent<ModifierReceiverComponent>()
			modifierComponent.modifierLogicList.forEach { (type, logic) ->
				logic.update(deltaTime)
				checkModifiers(entity, type, logic, modifierComponent)
			}
		}
	}

	private fun checkModifiers(
		entity: Entity,
		type: KClass<out IModifierData>,
		logic: IModifierLogic,
		modifierReceiverComponent: ModifierReceiverComponent
	) {
		if (logic.hasNoModifiers) {
			modifierReceiverComponent.modifierLogicList.remove(type)
			/*if (modifierReceiverComponent.modifierLogicList.isEmpty())
				EntityManager.removeComponent(entity, ActiveModifierComponent::class)*/
		}
	}
}