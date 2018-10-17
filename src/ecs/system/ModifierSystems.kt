package ecs.system

import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.PendingModifierReceiverComponent
import ecs.components.modifiers.PendingModifiersComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class ModifierUpdateSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(ModifierReceiverComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val modifierComponent = entity.getComponent(ModifierReceiverComponent::class)
			modifierComponent.timedModifiers.forEach { modifier ->
				modifier.update(deltaTime)
				if (!modifier.hasTimeLeft)
					modifierComponent.removeModifier(modifier)
			}
		}
	}
}

class ModifierAddSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PendingModifiersComponent::class).andInclude(ModifierReceiverComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val receiverComponent = entity.getComponent(ModifierReceiverComponent::class)
			val pendingComponent = entity.getComponent(PendingModifiersComponent::class)

			pendingComponent.factories.forEach {
				it.apply(receiverComponent)
			}

			EntityManager.removeComponent(entity, pendingComponent)
		}
	}
}

class InitializeModifierReceiverSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PendingModifierReceiverComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val pending = it.getComponent(PendingModifierReceiverComponent::class)
			val receiver = ModifierReceiverComponent(it, pending.bodyBuilder)
			EntityManager.addComponent(it, receiver)
			EntityManager.removeComponent(it, pending)
		}
	}

}