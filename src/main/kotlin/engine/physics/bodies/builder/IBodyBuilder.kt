package engine.physics.bodies.builder

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Physics
import engine.physics.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import jslib.pixi.Graphics
import utility.Rgba
import utility.Transform

interface IBodyBuilder {
	val transform: Transform
	val motionType: BodyMotionType
	val fillColor: Rgba
	val shape: IShape
	val restitution: Double
	val friction: Double
	val isSensor: Boolean
	val density: Double?

	fun buildBody(entity: Entity): IBody {
		return Physics.engine.createBody(transform.position, entity, shape) {
			it.motionType = motionType
			it.friction = friction
			it.restitution = restitution
			it.isSensor = isSensor
			it.angleRadians = transform.angleRadians

			val density = density
			if (density != null)
				it.density = density
		}
	}

	fun buildGraphics(): Graphics {
		return Graphics().apply {
			this.beginFill(fillColor.rgb, fillColor.alphaDouble)

			val shape = shape
			when (shape) {
				is Circle -> drawCircle(0, 0, shape.radius)
				is Rectangle -> {
					drawRect(0, 0, shape.width, shape.height)
					pivot.x = shape.width / 2.0
					pivot.y = shape.height / 2.0
				}
				else -> throw NotImplementedError("Shape ${shape::class.simpleName} is not yet supported")
			}

			endFill()

			position.x = transform.position.x
			position.y = transform.position.y
			rotation = transform.angleRadians
		}
	}
}