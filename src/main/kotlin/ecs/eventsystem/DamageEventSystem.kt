package ecs.eventsystem

import ecs.components.DamageComponent
import ecs.components.HealthComponent
import engine.entity.Entity
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType

class DamageEventSystem(physicsEventManager: PhysicsEventManager) :
	PhysicsEventSystem<DamageComponent, HealthComponent>(
		physicsEventManager,
		DamageComponent::class,
		HealthComponent::class
	) {

	override fun onTriggered(
		event: PhysicsEventType,
		entityA: Entity,
		componentA: DamageComponent,
		entityB: Entity,
		componentB: HealthComponent
	) {
		when (event) {
			PhysicsEventType.CollisionStart -> componentB.damagers.add(componentA)
			PhysicsEventType.CollisionEnd -> componentB.damagers.remove(componentA)
		}
	}

	init {
		subscribeOnCollisionStart()
		subscribeOnCollisionEnd()
	}
}