package ecs.eventsystem

import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.events.PhysicsCollisionEvent
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType
import game.modifiers.ModifierSupervisor

class ModifierEventSystem(physicsEventManager: PhysicsEventManager) {
	init {
		physicsEventManager.subscribe(PhysicsEventType.CollisionStart, this::onCollision)
	}

	private fun onCollision(collisionEvent: PhysicsCollisionEvent) {
		val entityA = collisionEvent.entityA
		val entityB = collisionEvent.entityB

		if (hasSpreader(entityA)) {
			if (hasReceiver(entityB))
				add(entityB, entityA)
		} else if (hasSpreader(entityB)) {
			if (hasReceiver(entityA))
				add(entityA, entityB)
		}
	}

	private fun add(receiverEntity: Entity, spreaderEntity: Entity) {
		ModifierSupervisor.addModifier(
			receiverEntity.getComponent(ModifierReceiverComponent::class),
			spreaderEntity.getComponent(ModifierSpreaderComponent::class).factory
		)
	}

	private fun hasReceiver(entity: Entity): Boolean =
		EntityManager.hasComponent(entity, ModifierReceiverComponent::class)

	private fun hasSpreader(entity: Entity): Boolean =
		EntityManager.hasComponent(entity, ModifierSpreaderComponent::class)
}