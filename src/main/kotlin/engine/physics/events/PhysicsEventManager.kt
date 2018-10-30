package engine.physics.events

typealias CollisionEventListener = (event: PhysicsCollisionEvent) -> Unit

abstract class PhysicsEventManager {
	private val eventListenerCollection = mutableMapOf<String, MutableCollection<CollisionEventListener>>()


	fun subscribe(event: PhysicsEventType, listener: CollisionEventListener) {
		if (!eventListenerCollection.containsKey(event.name)) {
			eventListenerCollection[event.name] = mutableListOf(listener)
			subscribeInternal(event)
		} else
			eventListenerCollection[event.name]!!.add(listener)
	}

	abstract fun subscribeInternal(event: PhysicsEventType)

	fun unsubscribe(event: PhysicsEventType, listener: CollisionEventListener) {
		val eventCollection = eventListenerCollection[event.name] ?: throw Error("Collections is null")

		when {
			eventCollection.size == 1 -> {
				eventListenerCollection.remove(event.name)
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

