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
	//var frictionStatic: Double = 0.5
	//var frictionAir: Double = 0.01
	//var density: Double? = null

	var lineWidth: Double = 0.0

	fun buildBody(entity: Entity): IBody {
		//todo maybe pass it as parameter?
		val body = shape!!.build(entity, position, Physics.engine.shapeBuilder)

		body.bodyMotionType = motionType
		body.friction = friction
		//body.frictionAir = frictionAir
		//body.frictionStatic = frictionStatic
		body.restitution = restitution

		return body
	}

	fun buildGraphics(): Graphics {
		return Graphics().apply {
			lineStyle(lineWidth, 0xFFFFFFFF)
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
