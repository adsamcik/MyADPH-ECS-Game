package engine.physics.bodies.builder

import engine.entity.Entity
import engine.interfaces.IMemento
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Physics
import engine.physics.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import jslib.pixi.Graphics
import kotlinx.serialization.Serializable
import utility.Double2
import utility.Rgba

@Serializable
class MutableBodyBuilder : IMutableBodyBuilder {
	override var motionType: BodyMotionType
	override var fillColor: Rgba = Rgba.NONE
	override var position: Double2 = Double2()
	override var shape: IShape

	override var restitution: Double = 0.0
	override var friction: Double = 0.1
	override var isSensor: Boolean = false
	override var density: Double? = null


	constructor(shape: IShape, motionType: BodyMotionType) {
		this.shape = shape
		this.motionType = motionType
	}

	constructor(shape: IShape, body: IBody) {
		position = body.position
		restitution = body.restitution
		friction = body.friction
		motionType = body.motionType
		isSensor = body.isSensor
		this.shape = shape
	}

	fun buildBody(entity: Entity): IBody {
		return Physics.engine.createBody(position, entity, shape) {
			it.motionType = motionType
			it.friction = friction
			it.restitution = restitution
			it.isSensor = isSensor

			val density = density
			if (density != null)
				it.density = density
		}
	}

	fun buildGraphics(): Graphics {
		return Graphics().apply {
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
		Memento(
			motionType,
			shape,
			fillColor,
			position,
			restitution,
			friction,
			isSensor
		)

	fun restore(memento: Memento) {
		motionType = memento.motionType
		shape = memento.shape
		fillColor = memento.fillColor
		position = memento.position
		restitution = memento.restitution
		friction = memento.friction
		isSensor = memento.isSensor
	}

	data class Memento(
		val motionType: BodyMotionType,
		val shape: IShape,
		val fillColor: Rgba,
		val position: Double2,
		val restitution: Double,
		val friction: Double,
		val isSensor: Boolean
	) : IMemento
}
