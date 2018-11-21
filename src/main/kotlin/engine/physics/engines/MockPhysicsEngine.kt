package engine.physics.engines

import engine.entity.Entity
import engine.physics.IShape
import engine.physics.bodies.IBody
import engine.physics.events.PhysicsEventType
import general.Double2
import tests.physics.MockBody

class MockPhysicsEngine : PhysicsEngine() {
	override val eventManager: MockPhysicsEventManager
		get() = MockPhysicsEventManager()

	private val activeBodyList = mutableListOf<MockBody>()

	override fun createBody(position: Double2, entity: Entity, shape: IShape) : IBody {
		val body = MockBody(shape, position, entity, this)
		activeBodyList.add(body)
		return body
	}

	override fun update(delta: Double) {
		activeBodyList.forEach {
			it.position += it.velocity * delta
		}
	}

	fun removeBody(body: MockBody) {
		activeBodyList.remove(body)
	}


	class MockPhysicsEventManager : engine.physics.events.PhysicsEventManager() {
		val map = mutableMapOf<PhysicsEventType, Boolean>()

		override fun subscribeInternal(event: PhysicsEventType) {
			map[event] = true
		}

		override fun unsubscribeInternal(event: PhysicsEventType) {
			map.remove(event)
		}

	}
}