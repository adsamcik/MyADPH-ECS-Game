package engine.physics.bodies

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import extensions.MathExtensions
import jslib.PlanckExtensions
import jslib.planck
import general.Double2

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
		get() = MathExtensions.toDegrees(angleRadians)
		set(value) {
			val radians = MathExtensions.toRadians(value)
			angleRadians = radians
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

	override var isEnabled: Boolean
		get() = body.isActive()
		set(value) {
			body.setActive(value)
		}

	override val filter = Filter(this)

	override fun applyForce(position: Double2, force: Double2) {
		body.applyForce(force.toVec2(), position.toVec2())
	}

	override fun applyForce(force: Double2) {
		body.applyForceToCenter((force * 100.0).toVec2(), true)
	}

	override fun rotate(degrees: Double) {
		val radians = MathExtensions.toRadians(degrees)
		body.setAngle(body.getAngle().toDouble() + radians)
	}

	override fun destroy() {
		world.destroyBody(body)
	}

	override fun wakeup() {
		if (!body.isAwake())
			body.setAwake(true)
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