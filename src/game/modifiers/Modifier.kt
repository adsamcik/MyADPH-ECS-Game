package game.modifiers

import ecs.components.modifiers.PendingModifiersComponent
import engine.entity.Entity
import engine.entity.EntityManager

object Modifier {
	fun addToEntity(entity: Entity, factory: ModifierCommandFactory) {
		if(EntityManager.hasComponent(entity, PendingModifiersComponent::class)) {
			val pendingComponent = EntityManager.getComponent(entity, PendingModifiersComponent::class)
			pendingComponent.addFactory(factory)
		} else {
			val pendingComponent = PendingModifiersComponent(factory)
			EntityManager.addComponent(entity, pendingComponent)
		}
	}
}