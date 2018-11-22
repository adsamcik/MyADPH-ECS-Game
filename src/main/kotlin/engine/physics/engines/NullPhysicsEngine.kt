package engine.physics.engines

import engine.entity.Entity
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.IBody
import engine.physics.events.PhysicsEventManager
import general.Double2

class NullPhysicsEngine  : PhysicsEngine() {
	private val message  = "Trying to use null physics engine, which is not valid. You must initialize proper physics engine."

	override val eventManager: PhysicsEventManager
		get() = throw Exception(message)

	override fun createBody(position: Double2, entity: Entity, shape: IShape): IBody {
		throw Exception(message)
	}

	override fun update(delta: Double) {
		throw Exception(message)
	}

}