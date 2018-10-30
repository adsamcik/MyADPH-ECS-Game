package engine.physics.events

import engine.entity.Entity


data class PhysicsCollisionEvent(
	val type: PhysicsEventType,
	val entityA: Entity,
	val entityB: Entity
) {

	val name
		get() = type.name

}