package engine.physics.bodies

import engine.entity.Entity
import engine.interfaces.IMemento
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Physics
import engine.physics.Rectangle
import jslib.pixi.Graphics
import kotlinx.serialization.Serializable
import utility.Double2
import utility.Rgba

@Serializable
class BodyBuilder {

	var motionType = BodyMotionType.Dynamic
	var fillColor: Rgba = Rgba.BLACK
	var position: Double2 = Double2()
	var shape: IShape

	var restitution: Double = 0.0
	var friction: Double = 0.1
	//todo implement density
	//var density: Double? = null


	constructor(shape: IShape) {
		this.shape = shape
	}

	constructor(shape: IShape, body: IBody) {
		position = body.position
		restitution = body.restitution
		friction = body.friction
		this.shape = shape
	}

	fun buildBody(entity: Entity): IBody {
		return Physics.engine.createBody(position, entity, shape) {
			it.motionType = motionType
			it.friction = friction
			it.restitution = restitution
		}
	}

	fun buildGraphics(): Graphics {
		return Graphics().apply {

			//disabled for now because there is no inner option
			/*if (lineWidth > 0.0)
				lineStyle(lineWidth, outlineColor.rgb)*/
			this.beginFill(fillColor.rgb)

			val shape = shape
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

	fun save() =
		Memento(motionType, shape, fillColor, position, restitution, friction)

	fun restore(memento: Memento) {
		motionType = memento.motionType
		shape = memento.shape
		fillColor = memento.fillColor
		position = memento.position
		restitution = memento.restitution
		friction = memento.friction
	}


	data class Memento(
		val motionType: BodyMotionType,
		val shape: IShape,
		val fillColor: Rgba,
		val position: Double2,
		val restitution: Double,
		val friction: Double
	) : IMemento
}
