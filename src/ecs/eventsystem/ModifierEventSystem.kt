package ecs.eventsystem

import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.events.PhysicsEvent
import engine.events.PhysicsEventManager
import game.modifiers.Modifier
import jslib.CollisionEvent

class ModifierEventSystem(physicsEventManager: PhysicsEventManager) {
	init {
		physicsEventManager.subscribe(PhysicsEvent.Type.CollisionStart, this::onCollision)
	}

	private fun onCollision(collisionEvent: CollisionEvent) {
		collisionEvent.pairs.forEach {
			val entityA = it.bodyA.entity
			val entityB = it.bodyB.entity

			if (hasSpreader(entityA)) {
				if (hasReceiver(entityB))
					Modifier.addToEntity(entityB, entityA.getComponent(ModifierSpreaderComponent::class).factory)
			} else if (hasSpreader(entityB)) {
				if (hasReceiver(entityA))
					Modifier.addToEntity(entityA, entityB.getComponent(ModifierSpreaderComponent::class).factory)
			}
		}
	}

	private fun hasReceiver(entity: Entity): Boolean = EntityManager.hasComponent(entity, ModifierReceiverComponent::class)

	private fun hasSpreader(entity: Entity): Boolean = EntityManager.hasComponent(entity, ModifierSpreaderComponent::class)
}