package engine.physics.events

import engine.entity.Entity

typealias CollisionEventListener = (event: PhysicsCollisionEvent) -> Unit

abstract class PhysicsEventManager {
	private val eventListenerCollection = mutableMapOf<String, MutableCollection<CollisionEventListener>>()


	fun subscribe(event: PhysicsEventType, listener: CollisionEventListener) {
		if (!eventListenerCollection.containsKey(event.eventName)) {
			eventListenerCollection[event.eventName] = mutableListOf(listener)
			subscribeInternal(event)
		} else
			eventListenerCollection[event.eventName]!!.add(listener)
	}

	abstract fun subscribeInternal(event: PhysicsEventType)

	fun unsubscribe(event: PhysicsEventType, listener: CollisionEventListener) {
		val eventCollection = eventListenerCollection[event.eventName] ?: throw Error("Collections is null")

		when {
			eventCollection.size == 1 -> {
				eventListenerCollection.remove(event.eventName)
				unsubscribeInternal(event)

			}
			else -> eventCollection.remove(listener)
		}
	}

	abstract fun unsubscribeInternal(event: PhysicsEventType)

	protected fun onCollision(collisionEvent: PhysicsCollisionEvent) {
		eventListenerCollection[collisionEvent.name]?.forEach { it.invoke(collisionEvent) }
	}
}

enum class PhysicsEventType {
	CollisionStart {
		override val eventName: String = PhysicsEventType.EVENT_COLLISION_START
	},
	CollisionEnd {
		override val eventName: String = PhysicsEventType.EVENT_COLLISION_END
	};

	abstract val eventName: String

	companion object {
		private const val EVENT_COLLISION_START = "collisionStart"
		private const val EVENT_COLLISION_END = "collisionEnd"
	}
}

data class PhysicsCollisionEvent(
	val type: PhysicsEventType,
	val entityA: Entity,
	val entityB: Entity
) {

	val name
		get() = type.name

}

