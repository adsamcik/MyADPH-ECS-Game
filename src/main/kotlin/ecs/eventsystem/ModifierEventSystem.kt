package ecs.eventsystem

import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.events.PhysicsCollisionEvent
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType
import game.modifiers.ModifierUtility

class ModifierEventSystem(physicsEventManager: PhysicsEventManager) {
	init {
		physicsEventManager.subscribe(PhysicsEventType.CollisionStart, this::onCollision)
	}

	private fun onCollision(collisionEvent: PhysicsCollisionEvent) {
		val entityA = collisionEvent.entityA
		val entityB = collisionEvent.entityB

		if (hasSpreader(entityA)) {
			if (hasReceiver(entityB))
				ModifierUtility.addToEntity(entityB, entityA.getComponent(ModifierSpreaderComponent::class).factory)
		} else if (hasSpreader(entityB)) {
			if (hasReceiver(entityA))
				ModifierUtility.addToEntity(entityA, entityB.getComponent(ModifierSpreaderComponent::class).factory)
		}
	}

	private fun hasReceiver(entity: Entity): Boolean =
		EntityManager.hasComponent(entity, ModifierReceiverComponent::class)

	private fun hasSpreader(entity: Entity): Boolean =
		EntityManager.hasComponent(entity, ModifierSpreaderComponent::class)
}