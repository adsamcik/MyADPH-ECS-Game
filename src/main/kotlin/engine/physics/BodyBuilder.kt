package engine.physics

import engine.entity.Entity
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import jslib.pixi.Graphics
import kotlinx.serialization.Serializable
import utility.Double2
import utility.Rgba

@Serializable
class BodyBuilder {
	var motionType = BodyMotionType.Dynamic
	var shape: IShape? = null
	var fillColor: Rgba = Rgba.BLACK
	var outlineColor: Rgba = Rgba.BLACK
	var position: Double2 = Double2()

	var restitution: Double = 0.0
	var friction: Double = 0.1
	//todo implement density
	//var density: Double? = null

	var lineWidth: Double = 0.0

	fun buildBody(entity: Entity): IBody {
		return Physics.engine.createBody(position, entity, shape!!) {
			it.bodyMotionType = motionType
			it.friction = friction
			it.restitution = restitution
		}
	}

	fun buildGraphics(): Graphics {
		return Graphics().apply {

			if (lineWidth > 0.0)
				lineStyle(lineWidth, outlineColor.rgb)
			this.beginFill(fillColor.rgb)

			val shape = shape!!
			when (shape) {
				is Circle -> drawCircle(position.x, position.y, shape.radius)
				is Rectangle -> {
					drawRect(0, 0, shape.width, shape.height)
					pivot.x = shape.width / 2.0
					pivot.y = shape.height / 2.0
				}
				else -> throw NotImplementedError("Shape ${shape::class.simpleName} is not yet supported")
			}

			endFill()
		}
	}
}
