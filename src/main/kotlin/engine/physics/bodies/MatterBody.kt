package engine.physics.bodies

import engine.entity.Entity
import jslib.Matter
import utility.Double2

class MatterBody(
	private val body: Matter.Body,
	override var entity: Entity,
	private val world: Matter.World
) : IBody {


	override var restitution: Double
		get() = body.restitution.toDouble()
		set(value) {
			body.restitution = value
		}

	override fun destroy() {
		Matter.World.remove(world, body)
	}

	init {
		Matter.World.add(world, body)
	}

	override var position: Double2
		get() = Double2(body.position)
		set(value) {
			Matter.Body.setPosition(body, value.toVector())
		}

	override var velocity: Double2
		get() = Double2(body.position)
		set(value) {
			Matter.Body.setVelocity(body, value.toVector())
			if (body.isSleeping)
				Matter.Sleeping.set(body, false)
		}

	override var angle: Double
		get() = body.angle.toDouble()
		set(value) {
			Matter.Body.setAngle(body, value)
		}

	override var bodyMotionType: BodyMotionType
		get() = when {
			body.isStatic -> BodyMotionType.Static
			else -> BodyMotionType.Kinematic
		}
		set(value) {
			when (value) {
				BodyMotionType.Static, BodyMotionType.Kinematic -> Matter.Body.setStatic(body, true)
				BodyMotionType.Dynamic -> Matter.Body.setStatic(body, false)
			}
		}

	override var friction: Double
		get() = body.friction.toDouble()
		set(value) {
			body.friction = value
		}

	override fun applyForce(position: Double2, force: Double2) {
		Matter.Body.applyForce(body, position.toVector(), force.toVector())
	}

	override fun rotate(degrees: Double) {
		Matter.Body.rotate(body, degrees)
	}

}