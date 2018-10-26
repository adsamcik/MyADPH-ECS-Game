package engine.physics.events

import engine.physics.bodies.getTypedUserData
import jslib.*

class PlanckEventManager(private val world: planck.World) : PhysicsEventManager() {
	override fun subscribeInternal(event: PhysicsEventType) {
		when (event) {
			PhysicsEventType.CollisionStart -> world.onBeginContact(this::onBeginContact)
			PhysicsEventType.CollisionEnd -> world.onEndContact(this::onEndContact)
		}
	}

	override fun unsubscribeInternal(event: PhysicsEventType) {
		when (event) {
			PhysicsEventType.CollisionStart -> world.offBeginContact(this::onBeginContact)
			PhysicsEventType.CollisionEnd -> world.offEndContact(this::onEndContact)
		}
	}

	private fun onBeginContact(contact: planck.Contact) {
		onContact(PhysicsEventType.CollisionStart, contact)
	}

	private fun onEndContact(contact: planck.Contact) {
		onContact(PhysicsEventType.CollisionEnd, contact)
	}

	private fun onContact(type: PhysicsEventType, contact: planck.Contact) {
		val dataA = contact.getFixtureA().getTypedUserData()
		val dataB = contact.getFixtureB().getTypedUserData()
		val collisionEvent = PhysicsCollisionEvent(type, dataA.entity, dataB.entity)
		onCollision(collisionEvent)
	}

}