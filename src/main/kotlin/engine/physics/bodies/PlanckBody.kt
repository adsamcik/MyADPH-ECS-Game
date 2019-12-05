package engine.physics.bodies

import definition.jslib.FixtureDef
import definition.jslib.PlanckExtensions
import definition.jslib.planck
import engine.entity.Entity
import extensions.toDegrees
import extensions.toRadians
import general.Double2
import kotlin.js.json

class PlanckBody(
	shape: planck.Shape,
	position: Double2,
	entity: Entity,
	private val world: planck.World,
	density: Double = 1.0
) : IBody {
	val body: planck.Body = world.createBody(position.toVec2())
	private val fixture =
		body.createFixture(shape, FixtureDef(density = density, userData = PhysicsData(entity)))

	private val data
		get() = fixture.getUserData().unsafeCast<PhysicsData>()

	override var entity: Entity
		get() = data.entity
		set(value) {
			data.entity = value
		}

	override var restitution: Double
		get() = requireNotNull(body.getFixtureList()).getRestitution().toDouble()
		set(value) {
			requireNotNull(body.getFixtureList()).setRestitution(value)
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
		get() = angleRadians.toDegrees()
		set(value) {
			angleRadians = value.toRadians()
		}

	override var angleRadians: Double
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
				BodyMotionType.Dynamic -> {
					body.setDynamic()
					body.setBullet(true)
				}
			}
		}
	override var friction: Double
		get() = fixture.getFriction().toDouble()
		set(value) {
			//friction itself didn't work :)
			body.setLinearDamping(value)
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

	override var mass: Double
		get() = body.getMass().toDouble()
		set(value) {
			val massData = object : planck.MassData {
				override var mass: Number = 0
				override var center: planck.Vec2 = planck.Vec2(0, 0)
				override var I: Number = 0
			}
			body.getMassData(massData)
			massData.mass = value
			body.setMassData(massData)
		}

	override var isEnabled: Boolean
		get() = body.isActive()
		set(value) {
			body.setActive(value)
		}

	override var isAwake: Boolean
		get() = body.isAwake()
		set(value) {
			body.setAwake(value)
		}

	override val filter = Filter(this)

	override fun applyForce(position: Double2, force: Double2) {
		body.applyForce(force.toVec2(), position.toVec2())
	}

	override fun applyForce(force: Double2) {
		body.applyForceToCenter((force * 100.0).toVec2(), true)
	}

	override fun rotate(degrees: Double) {
		body.setAngle(angleRadians + degrees.toRadians())
	}

	override fun destroy() {
		world.destroyBody(body)
	}

	data class PhysicsData(var entity: Entity)

	class Filter(private val body: PlanckBody) : IBody.IFilter {
		override var group: Int
			get() = body.fixture.getFilterGroupIndex()
			set(value) {
				body.fixture.setFilterData(PlanckExtensions.FilterObject(value, category, mask))
			}

		override var category: Int
			get() = body.fixture.getFilterCategoryBits()
			set(value) {
				body.fixture.setFilterData(PlanckExtensions.FilterObject(group, value, mask))
			}

		override var mask: Int
			get() = body.fixture.getFilterMaskBits()
			set(value) {
				body.fixture.setFilterData(PlanckExtensions.FilterObject(group, category, value))
			}

		override fun set(group: Int, category: Int, mask: Int) {
			body.fixture.setFilterData(PlanckExtensions.FilterObject(group, category, mask))
		}
	}
}

fun planck.Fixture.getTypedUserData() = getUserData().unsafeCast<PlanckBody.PhysicsData>()