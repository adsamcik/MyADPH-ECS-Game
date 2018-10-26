package engine.physics.bodies

import engine.entity.Entity
import jslib.planck
import utility.Double2

class PlanckBody(
	val shape: planck.Shape,
	entity: Entity,
	world: planck.World
) : IBody {

	val body: planck.Body = world.createBody()
	private val fixture = body.createFixture(shape)

	private val data
		get() = fixture.getUserData().unsafeCast<PhysicsData>()

	init {
		fixture.setUserData(PhysicsData(entity))
	}

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

	override var bodyMotionType: BodyMotionType
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
		get() = body.getLinearDamping().toDouble()
		set(value) {
			body.setLinearDamping(value)
		}

	override fun applyForce(position: Double2, force: Double2) {
		body.applyForce(position.toVec2(), force.toVec2())
	}

	override fun rotate(degrees: Double) {
		body.setTransform(body.getPosition(), degrees)
	}

	override fun destroy() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	data class PhysicsData(var entity: Entity)
}

fun planck.Fixture.getTypedUserData() = getUserData().unsafeCast<PlanckBody.PhysicsData>()