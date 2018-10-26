package engine.physics.engines

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import engine.physics.bodies.IBody
import engine.physics.events.PhysicsEventManager
import utility.Double2

abstract class PhysicsEngine {
	abstract val eventManager: PhysicsEventManager
	abstract val shapeBuilder: IPhysicsShapeBuilder

	fun createBody(entity: Entity, shape: IShape, builder: (IBody) -> Unit): IBody {
		val body = shape.build(entity, Double2(), shapeBuilder)
		builder.invoke(body)
		return body
	}

	abstract fun update(deltaMs: Int)
}

interface IPhysicsShapeBuilder {
	fun build(entity: Entity, position: Double2, rectangle: Rectangle): IBody
	fun build(entity: Entity, position: Double2, shape: Circle): IBody
	fun build(entity: Entity, position: Double2, polygon: Polygon): IBody
}