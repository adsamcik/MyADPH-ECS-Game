package engine.physics.events

import jslib.CollisionEvent
import jslib.Matter

class MatterEventManager(private val physicsEngine: Matter.Engine) : PhysicsEventManager() {
	override fun subscribeInternal(event: PhysicsEventType) {
		Matter.Events.on(physicsEngine, event.eventName, this::onCollisionEvent)
	}

	override fun unsubscribeInternal(event: PhysicsEventType) {
		Matter.Events.off(physicsEngine, event.eventName, this::onCollisionEvent)
	}

	private fun onCollisionEvent(event: CollisionEvent) {
		val type = PhysicsEventType.valueOf(event.name)
		val pair = event.pairs.first()
		val entityA = pair.bodyA.entity
		val entityB = event.pairs.first().bodyB.entity
		val collisionEvent = PhysicsCollisionEvent(type, entityA, entityB)

		onCollision(collisionEvent)
	}
}

