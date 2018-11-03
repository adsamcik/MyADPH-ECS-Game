package game.modifiers

import ecs.components.GraphicsComponent
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.PendingModifiersComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import engine.physics.bodies.IBody

object ModifierUtility {
	fun addToEntity(entity: Entity, factory: ModifierCommandFactory) {
		val receiver = entity.getComponent(ModifierReceiverComponent::class)
		if(EntityManager.hasComponent(entity, PendingModifiersComponent::class)) {
			val pendingComponent = EntityManager.getComponent(entity, PendingModifiersComponent::class)
			pendingComponent.addFactory(factory)
		} else {
			val pendingComponent = PendingModifiersComponent(factory)
			EntityManager.addComponent(entity, pendingComponent)
		}
	}
}