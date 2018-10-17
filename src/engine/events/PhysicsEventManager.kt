package engine.events

import CollisionEvent
import Matter

typealias CollisionEventListener = (event: CollisionEvent) -> Unit

class PhysicsEventManager(private val physicsEngine: Matter.Engine) {

	private val eventListenerCollection = mutableMapOf<String, MutableCollection<CollisionEventListener>>()

	fun subscribe(event: PhysicsEvent.Type, listener: CollisionEventListener) {
		if (!eventListenerCollection.containsKey(event.eventName)) {
			Matter.Events.on(physicsEngine, event.eventName, this::onCollisionEvent)
			eventListenerCollection[event.eventName] = mutableListOf(listener)
		} else
			eventListenerCollection[event.eventName]!!.add(listener)
	}

	fun unsubscribe(event: PhysicsEvent.Type, listener: CollisionEventListener) {
		val eventCollection = eventListenerCollection[event.eventName] ?: throw Error("Collections is null")
		eventCollection.remove(listener)

		if (eventCollection.isEmpty()) {
			Matter.Events.on(physicsEngine, event.eventName, this::onCollisionEvent)
			eventListenerCollection.remove(event.eventName)
		}
	}

	private fun onCollisionEvent(event: CollisionEvent) {
		eventListenerCollection[event.name]?.forEach { it.invoke(event) }
	}
}

data class PhysicsEvent(val type: Type) {

	enum class Type {
		AfterAdd {
			override val eventName: String = EVENT_AFTER_ADD
		},
		BeforeUpdate {
			override val eventName: String = EVENT_BEFORE_UPDATE
		},
		CollisionStart {
			override val eventName: String = EVENT_COLLISION_START
		},
		CollisionActive {
			override val eventName: String = EVENT_COLLISION_ACTIVE
		},
		CollisionEnd {
			override val eventName: String = EVENT_COLLISION_END
		};

		abstract val eventName: String
	}

	companion object {
		private const val EVENT_AFTER_ADD = "afterAdd"
		private const val EVENT_BEFORE_UPDATE = "beforeUpdate"
		private const val EVENT_COLLISION_START = "collisionStart"
		private const val EVENT_COLLISION_ACTIVE = "collisionActive"
		private const val EVENT_COLLISION_END = "collisionEnd"
	}
}

