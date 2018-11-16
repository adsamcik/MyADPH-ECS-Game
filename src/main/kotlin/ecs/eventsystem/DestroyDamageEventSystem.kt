package ecs.eventsystem

import ecs.components.health.HealthComponent
import ecs.components.health.InstantDestructionComponent
import engine.entity.Entity
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType

class DestroyDamageEventSystem(physicsEventManager: PhysicsEventManager) :
	PhysicsEventSystem<InstantDestructionComponent, HealthComponent>(
		physicsEventManager,
		InstantDestructionComponent::class,
		HealthComponent::class
	) {

	override fun onTriggered(
		event: PhysicsEventType,
		entityA: Entity,
		componentA: InstantDestructionComponent,
		entityB: Entity,
		componentB: HealthComponent
	) {
		componentB.health = Double.NEGATIVE_INFINITY
	}

	init {
		subscribeOnCollisionStart()
		subscribeOnCollisionEnd()
	}

}