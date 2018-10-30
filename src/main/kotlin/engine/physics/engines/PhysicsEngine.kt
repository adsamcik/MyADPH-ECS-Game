package engine.physics.engines

import engine.entity.Entity
import engine.physics.IShape
import engine.physics.bodies.IBody
import engine.physics.events.PhysicsEventManager
import utility.Double2

abstract class PhysicsEngine {
	abstract val eventManager: PhysicsEventManager

	abstract fun createBody(position: Double2, entity: Entity, shape: IShape): IBody

	fun createBody(
		position: Double2 = Double2(),
		entity: Entity,
		shape: IShape,
		builder: (IBody) -> Unit
	): IBody {
		val body = createBody(position, entity, shape)
		builder.invoke(body)
		return body
	}

	abstract fun update(delta: Double)
}