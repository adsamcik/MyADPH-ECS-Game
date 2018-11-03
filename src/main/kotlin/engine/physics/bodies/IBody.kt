package engine.physics.bodies

import engine.entity.Entity
import engine.interfaces.IMemento
import utility.Double2

interface IBody {
	var position: Double2

	var velocity: Double2

	var angle: Double

	var bodyMotionType: BodyMotionType

	var friction: Double

	var restitution: Double

	var entity: Entity

	fun applyForce(position: Double2, force: Double2)
	fun rotate(degrees: Double)
	fun wakeup()

	fun destroy()

	//exposes only memento interface because it should be black box
	fun save(): IMemento = Memento(position, velocity, angle, bodyMotionType, friction, restitution, entity)
	fun restore(memento: IMemento) {
		if (memento is IBody.Memento)
			restore(memento)
		else
			throw IllegalArgumentException("Expected memento of type ${IBody.Memento::class} but got ${memento::class}")
	}

	private fun restore(memento: Memento) {
		position = memento.position
		velocity = memento.velocity
		angle = memento.angle
		bodyMotionType = memento.bodyMotionType
		friction = memento.friction
		restitution = memento.restitution
		entity = memento.entity
	}

	data class Memento(
		val position: Double2,
		val velocity: Double2,
		val angle: Double,
		val bodyMotionType: BodyMotionType,
		val friction: Double,
		val restitution: Double,
		val entity: Entity
	) : IMemento
}

enum class BodyMotionType {
	Static,
	Kinematic,
	Dynamic
}

enum class BodyType {
	Circle,
	Rectangle
}