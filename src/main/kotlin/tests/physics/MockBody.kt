package tests.physics

import engine.entity.Entity
import engine.physics.IShape
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import engine.physics.engines.MockPhysicsEngine
import extensions.MathExtensions
import general.Double2

class MockBody(
	var shape: IShape,
	override var position: Double2,
	override var entity: Entity,
	val engine: MockPhysicsEngine
) : IBody {

	override var velocity: Double2 = Double2()

	override var angle: Double
		get() = MathExtensions.toDegrees(angleRadians)
		set(value) {
			MathExtensions.toRadians(value)
		}

	override var angleRadians: Double = 0.0

	override var motionType: BodyMotionType = BodyMotionType.Dynamic
	override var friction: Double = 0.0

	override var restitution: Double = 0.0

	override var isSensor: Boolean = false

	override var density: Double = 0.0

	override var isEnabled: Boolean = true

	override val filter: IBody.IFilter = Filter(0, 0, 0)

	override fun applyForce(position: Double2, force: Double2) {
		velocity += force
	}

	override fun applyForce(force: Double2) {
		velocity += force
	}

	override fun rotate(degrees: Double) {
		angle += degrees
	}

	override fun wakeup() {

	}

	override fun destroy() {
		engine.removeBody(this)
	}

	data class Filter(override var group: Int, override var category: Int, override var mask: Int) : IBody.IFilter {
		override fun set(group: Int, category: Int, mask: Int) {
			this.group = group
			this.category = category
			this.mask = mask
		}
	}
}