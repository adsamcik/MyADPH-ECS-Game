package engine.physics.bodies.builder

import engine.interfaces.IMemento
import engine.physics.IShape
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import kotlinx.serialization.Serializable
import utility.Double2
import utility.Rgba

@Serializable
class MutableBodyBuilder : IMutableBodyBuilder {
	override var motionType: BodyMotionType
	override var fillColor: Rgba = Rgba.NONE
	override var position: Double2 = Double2()
	override var shape: IShape
	override var angle: Double = 0.0

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

	constructor(bodyBuilder: IBodyBuilder) {
		motionType = bodyBuilder.motionType
		fillColor = bodyBuilder.fillColor
		position = bodyBuilder.position
		shape = bodyBuilder.shape
		restitution = bodyBuilder.restitution
		friction = bodyBuilder.friction
		isSensor = bodyBuilder.isSensor
		density = bodyBuilder.density
	}

	fun save() =
		Memento(
			motionType,
			shape,
			fillColor,
			position,
			restitution,
			friction,
			isSensor,
			density
		)

	fun restore(memento: Memento) {
		motionType = memento.motionType
		shape = memento.shape
		fillColor = memento.fillColor
		position = memento.position
		restitution = memento.restitution
		friction = memento.friction
		isSensor = memento.isSensor
		density = memento.density
	}

	data class Memento(
		val motionType: BodyMotionType,
		val shape: IShape,
		val fillColor: Rgba,
		val position: Double2,
		val restitution: Double,
		val friction: Double,
		val isSensor: Boolean,
		val density: Double?
	) : IMemento
}
