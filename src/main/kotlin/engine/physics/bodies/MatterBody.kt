package engine.physics.bodies

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import engine.physics.engines.MatterPhysicsEngine.Companion.MATTER_SCALE
import jslib.Matter
import utility.Double2

class MatterBody(
	shape: IShape,
	position: Double2,
	entity: Entity,
	private val world: Matter.World
) : IBody {

	private val body: Matter.Body

	override var entity: Entity
		get() = body.entity
		set(value) {
			body.entity = value
		}

	init {
		this.body = when (shape) {
			is Rectangle -> buildShape(shape, position)
			is Circle -> buildShape(shape, position)
			is Polygon -> buildShape(shape, position)
			else -> throw NotImplementedError("Shape builder for shape ${shape::class.simpleName} not implemented")
		}

		this.body.entity = entity

		Matter.World.add(world, this.body)
	}

	private fun buildShape(
		rectangle: Rectangle,
		position: Double2
	) = Matter.Bodies.rectangle(
		position.x * MATTER_SCALE,
		position.y * MATTER_SCALE,
		rectangle.width * MATTER_SCALE,
		rectangle.height * MATTER_SCALE
	)

	private fun buildShape(
		circle: Circle,
		position: Double2
	) = Matter.Bodies.circle(
		position.x * MATTER_SCALE,
		position.y * MATTER_SCALE,
		circle.radius * MATTER_SCALE
	)


	private fun buildShape(
		polygon: Polygon,
		position: Double2
	) = Matter.Bodies.fromVertices(
		position.x * MATTER_SCALE,
		position.y * MATTER_SCALE,
		polygon.points.map { it * MATTER_SCALE }.toTypedArray()
	)

	override var restitution: Double
		get() = body.restitution.toDouble()
		set(value) {
			body.restitution = value
		}

	override var position: Double2
		get() = Double2(body.position) / MATTER_SCALE
		set(value) {
			val scaledPosition = value * MATTER_SCALE
			Matter.Body.setPosition(body, scaledPosition.toVector())
			wakeup()
		}

	override var velocity: Double2
		get() = Double2(body.velocity) / MATTER_SCALE
		set(value) {
			val scaledVelocity = value * MATTER_SCALE
			Matter.Body.setVelocity(body, scaledVelocity.toVector())
			wakeup()
		}

	override var angle: Double
		get() = body.angle.toDouble()
		set(value) {
			Matter.Body.setAngle(body, value)
			wakeup()
		}

	override var isSensor: Boolean
		get() = body.isSensor
		set(value) {
			body.isSensor = value
		}

	override var bodyMotionType: BodyMotionType
		get() = when {
			body.isStatic -> BodyMotionType.Static
			else -> BodyMotionType.Dynamic
		}
		set(value) {
			when (value) {
				BodyMotionType.Static, BodyMotionType.Kinematic -> Matter.Body.setStatic(body, true)
				BodyMotionType.Dynamic -> {
					Matter.Body.setStatic(body, false)
					wakeup()
				}
			}
		}

	override var friction: Double
		get() = body.friction.toDouble()
		set(value) {
			body.friction = value
		}

	override fun destroy() {
		Matter.World.remove(world, body)
	}

	override fun applyForce(position: Double2, force: Double2) {
		Matter.Body.applyForce(body, (position * MATTER_SCALE).toVector(), (force * MATTER_SCALE).toVector())
	}

	override fun rotate(degrees: Double) {
		Matter.Body.rotate(body, degrees)
	}

	override fun wakeup() {
		if (body.isSleeping)
			Matter.Sleeping.set(body, false)
	}

}