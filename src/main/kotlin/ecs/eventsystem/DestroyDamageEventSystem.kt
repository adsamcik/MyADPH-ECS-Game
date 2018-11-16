package ecs.eventsystem

import ecs.components.health.DestroyDamageComponent
import ecs.components.health.HealthComponent
import engine.entity.Entity
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType

class DestroyDamageEventSystem(physicsEventManager: PhysicsEventManager) :
	PhysicsEventSystem<DestroyDamageComponent, HealthComponent>(
		physicsEventManager,
		DestroyDamageComponent::class,
		HealthComponent::class
	) {

	override fun onTriggered(
		event: PhysicsEventType,
		entityA: Entity,
		componentA: DestroyDamageComponent,
		entityB: Entity,
		componentB: HealthComponent
	) {
		componentB.health = Double.NEGATIVE_INFINITY
	}

	init {
		subscribeOnCollisionStart()
	}

}