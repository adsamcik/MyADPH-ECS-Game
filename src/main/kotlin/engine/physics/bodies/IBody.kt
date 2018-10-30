package engine.physics.bodies

import engine.entity.Entity
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