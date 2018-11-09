package engine.physics.bodies.builder

import engine.physics.IShape
import engine.physics.bodies.BodyMotionType
import utility.Double2
import utility.Rgba

interface IBodyBuilder {
	val motionType: BodyMotionType
	val fillColor: Rgba
	val position: Double2
	val shape: IShape
	val restitution: Double
	val friction: Double
	val isSensor: Boolean
	val density: Double?
}