package engine.physics.bodies.builder

import engine.physics.IShape
import engine.physics.bodies.BodyMotionType
import utility.Double2
import utility.Rgba

interface IMutableBodyBuilder : IBodyBuilder {
	override var motionType: BodyMotionType
	override var fillColor: Rgba
	override var position: Double2
	override var shape: IShape
	override var restitution: Double
	override var friction: Double
	override var isSensor: Boolean
	override var density: Double?
	override var angle: Double
}