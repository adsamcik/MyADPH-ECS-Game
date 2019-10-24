package engine.physics.events

import definition.jslib.CollisionEvent
import definition.jslib.Matter

class MatterEventManager(private val physicsEngine: Matter.Engine) : PhysicsEventManager() {

	private fun getMatterEventName(eventType: PhysicsEventType) = when (eventType) {
		PhysicsEventType.CollisionStart -> EVENT_COLLISION_START
		PhysicsEventType.CollisionEnd -> EVENT_COLLISION_END
	}

	private fun getEventType(eventName: String) = when (eventName) {
		EVENT_COLLISION_START -> PhysicsEventType.CollisionStart
		EVENT_COLLISION_END -> PhysicsEventType.CollisionEnd
		else -> throw IllegalArgumentException("Unknown event name $eventName")
	}

	override fun subscribeInternal(event: PhysicsEventType) {
		Matter.Events.on(physicsEngine, getMatterEventName(event), this::onCollisionEvent)
	}

	override fun unsubscribeInternal(event: PhysicsEventType) {
		Matter.Events.off(physicsEngine, getMatterEventName(event), this::onCollisionEvent)
	}

	private fun onCollisionEvent(event: CollisionEvent) {
		val type = getEventType(event.name)
		val pair = event.pairs.first()
		val entityA = pair.bodyA.userData.entity
		val entityB = pair.bodyB.userData.entity
		val collisionEvent = PhysicsCollisionEvent(type, entityA, entityB)

		onCollision(collisionEvent)
	}

	companion object {
		const val EVENT_COLLISION_START = "collisionStart"
		const val EVENT_COLLISION_END = "collisionEnd"
	}
}

