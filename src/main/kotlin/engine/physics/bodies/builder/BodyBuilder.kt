package engine.physics.bodies.builder

import engine.physics.IShape
import engine.physics.bodies.BodyMotionType
import utility.Double2
import utility.Rgba

data class BodyBuilder(
	override val motionType: BodyMotionType,
	override val fillColor: Rgba,
	override val position: Double2,
	override val shape: IShape,
	override val restitution: Double,
	override val friction: Double,
	override val isSensor: Boolean,
	override val density: Double?
) : IBodyBuilder {
	constructor(bodyBuilder: BodyBuilder) : this(
		bodyBuilder.motionType,
		bodyBuilder.fillColor,
		bodyBuilder.position,
		bodyBuilder.shape,
		bodyBuilder.restitution,
		bodyBuilder.friction,
		bodyBuilder.isSensor,
		bodyBuilder.density
	)
}