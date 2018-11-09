package engine.physics.bodies

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import jslib.planck
import utility.Double2

class PlanckBody(
	shape: IShape,
	position: Double2,
	entity: Entity,
	private val world: planck.World
) : IBody {
	val body: planck.Body = world.createBody(position.toVec2())
	private val fixture = body.createFixture(buildShape(shape))

	private val data
		get() = fixture.getUserData().unsafeCast<PhysicsData>()

	init {
		fixture.setUserData(PhysicsData(entity))
	}

	private fun buildShape(shape: IShape): planck.Shape {
		return when (shape) {
			is Rectangle -> buildShape(shape)
			is Circle -> buildShape(shape)
			is Polygon -> buildShape(shape)
			else -> throw IllegalArgumentException("Shape ${shape::class.simpleName} not supported")
		}
	}

	private fun buildShape(rectangle: Rectangle) = planck.Box(rectangle.width / 2, rectangle.height / 2)

	private fun buildShape(circle: Circle) = planck.Circle(circle.radius)

	private fun buildShape(polygon: Polygon) = planck.Polygon(polygon.points.map { it.toVec2() }.toTypedArray())

	override var entity: Entity
		get() = data.entity
		set(value) {
			data.entity = value
		}

	override var restitution: Double
		get() = body.getFixtureList()!!.getRestitution().toDouble()
		set(value) {
			body.getFixtureList()!!.setRestitution(value)
		}

	override var position: Double2
		get() = Double2(body.getPosition())
		set(value) {
			body.setPosition(value.toVec2())
		}

	override var velocity: Double2
		get() = Double2(body.getLinearVelocity())
		set(value) {
			body.setLinearVelocity(value.toVec2())
		}
	override var angle: Double
		get() = body.getAngle().toDouble()
		set(value) {
			body.setAngle(value)
		}

	override var motionType: BodyMotionType
		get() {
			return when {
				body.isDynamic() -> BodyMotionType.Dynamic
				body.isStatic() -> BodyMotionType.Static
				body.isKinematic() -> BodyMotionType.Kinematic
				else -> throw IllegalStateException()
			}
		}
		set(value) {
			when (value) {
				BodyMotionType.Static -> body.setStatic()
				BodyMotionType.Kinematic -> body.setKinematic()
				BodyMotionType.Dynamic -> body.setDynamic()
			}
		}
	override var friction: Double
		get() = fixture.getFriction().toDouble()
		set(value) {
			fixture.setFriction(value)
		}

	override var isSensor: Boolean
		get() = fixture.isSensor()
		set(value) {
			fixture.setSensor(value)
		}

	override var density: Double
		get() = fixture.getDensity().toDouble()
		set(value) {
			fixture.setDensity(value)
		}

	override fun applyForce(position: Double2, force: Double2) {
		body.applyForce(force.toVec2(), position.toVec2())
	}

	fun applyForce(force: Double2) {
		body.applyForceToCenter((force * 100.0).toVec2(), true)
	}

	override fun rotate(degrees: Double) {
		body.setTransform(body.getPosition(), body.getAngle().toDouble() + degrees)
	}

	override fun destroy() {
		world.destroyBody(body)
	}

	override fun wakeup() {
		if (!body.isAwake())
			body.setAwake(true)
	}

	data class PhysicsData(var entity: Entity)
}

fun planck.Fixture.getTypedUserData() = getUserData().unsafeCast<PlanckBody.PhysicsData>()