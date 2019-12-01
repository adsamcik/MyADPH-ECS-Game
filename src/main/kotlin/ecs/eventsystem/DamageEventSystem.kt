package ecs.eventsystem

import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
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
			PhysicsEventType.CollisionStart -> componentB.damageList.add(componentA)
			PhysicsEventType.CollisionEnd -> componentB.damageList.remove(componentA)
		}
	}

	init {
		subscribeOnCollisionStart()
		subscribeOnCollisionEnd()
	}
}