package engine.physics.bodies.builder

import engine.physics.IShape
import engine.physics.bodies.BodyMotionType
import engine.types.Rgba
import engine.types.Transform

interface IMutableBodyBuilder : IBodyBuilder {
	override var motionType: BodyMotionType
	override var fillColor: Rgba
	override var transform: Transform
	override var shape: IShape
	override var restitution: Double
	override var friction: Double
	override var isSensor: Boolean
	override var density: Double?
}