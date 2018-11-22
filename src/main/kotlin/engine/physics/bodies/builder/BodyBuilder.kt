package engine.physics.bodies.builder

import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.BodyMotionType
import engine.types.Rgba
import engine.types.Transform

data class BodyBuilder(
	override val motionType: BodyMotionType,
	override val fillColor: Rgba,
	override val transform: Transform,
	override val shape: IShape,
	override val restitution: Double,
	override val friction: Double,
	override val isSensor: Boolean,
	override val density: Double?
) : IBodyBuilder {
	constructor(bodyBuilder: BodyBuilder) : this(
		bodyBuilder.motionType,
		bodyBuilder.fillColor,
		bodyBuilder.transform,
		bodyBuilder.shape,
		bodyBuilder.restitution,
		bodyBuilder.friction,
		bodyBuilder.isSensor,
		bodyBuilder.density
	)
}