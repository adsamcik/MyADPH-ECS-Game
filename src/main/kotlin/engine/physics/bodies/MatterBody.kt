package engine.physics.bodies

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import engine.physics.engines.MatterPhysicsEngine.Companion.MATTER_SCALE
import jslib.Matter
import jslib.UserData
import utility.Double2

class MatterBody(
	shape: IShape,
	position: Double2,
	entity: Entity,
	private val world: Matter.World
) : IBody {
	private val body: Matter.Body

	override var entity: Entity
		get() = body.userData.entity
		set(value) {
			body.userData.entity = value
		}

	init {
		this.body = when (shape) {
			is Rectangle -> buildShape(shape, position)
			is Circle -> buildShape(shape, position)
			is Polygon -> buildShape(shape, position)
			else -> throw NotImplementedError("Shape builder for shape ${shape::class.simpleName} not implemented")
		}

		this.body.userData = UserData(entity)

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

	override var motionType: BodyMotionType
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

	override var density: Double
		get() = body.density.toDouble()
		set(value) {
			Matter.Body.setDensity(body, value)
		}

	override var filter = Filter(body)

	override var isEnabled: Boolean
		get() = filter.isEnabled()
		set(value) {
			if (value == isEnabled)
				return

			if (value) {
				filter.enable()
				Matter.Sleeping.set(body, false)
			} else {
				filter.disable()
				Matter.Sleeping.set(body, true)
			}
		}

	override fun destroy() {
		Matter.World.remove(world, body)
	}

	override fun applyForce(position: Double2, force: Double2) {
		Matter.Body.applyForce(body, (position * MATTER_SCALE).toVector(), (force * MATTER_SCALE).toVector())
	}

	override fun applyForce(force: Double2) {
		applyForce(position, force)
	}


	override fun rotate(degrees: Double) {
		Matter.Body.rotate(body, degrees)
	}

	override fun wakeup() {
		if (body.isSleeping)
			Matter.Sleeping.set(body, false)
	}


	class Filter(private val body: Matter.Body) : IBody.IFilter {
		override var group: Int
			get() = body.collisionFilter.group
			set(value) {
				val originalState = originalState
				if (originalState != null)
					this.originalState = IBody.IFilter.Memento(value, originalState.category, originalState.mask)
				else
					body.collisionFilter.group = value
			}

		override var category: Int
			get() = body.collisionFilter.category
			set(value) {
				val originalState = originalState
				if (originalState != null)
					this.originalState = IBody.IFilter.Memento(originalState.group, value, originalState.mask)
				else
					body.collisionFilter.category = value
			}

		override var mask: Int
			get() = body.collisionFilter.mask
			set(value) {
				val originalState = originalState
				if (originalState != null)
					this.originalState = IBody.IFilter.Memento(originalState.group, originalState.category, value)
				else
					body.collisionFilter.mask = value
			}

		private var originalState: IBody.IFilter.Memento? = null

		override fun set(group: Int, category: Int, mask: Int) {
			if (originalState != null)
				originalState = IBody.IFilter.Memento(group, category, mask)
			else {
				body.collisionFilter.group = group
				body.collisionFilter.category = category
				body.collisionFilter.mask = mask
			}
		}

		fun enable() {
			restore(originalState!!)
			originalState = null
		}

		fun disable() {
			originalState = save()
			mask = 0
		}

		fun isEnabled() = originalState == null
	}
}