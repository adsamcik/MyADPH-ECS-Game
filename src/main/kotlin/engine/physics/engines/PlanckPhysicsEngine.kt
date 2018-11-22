package engine.physics.engines

import engine.entity.Entity
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.shapes.builders.PlanckShapeBuilder
import engine.physics.events.PlanckEventManager
import general.Double2
import jslib.planck
import kotlin.math.roundToInt

class PlanckPhysicsEngine : PhysicsEngine() {

	val world = planck.World()

	init {
		world.setGravity(planck.Vec2(0, 4.9))
	}

	override fun update(delta: Double) {
		val scaledDelta = delta * 10
		val iterations = (delta / 1.0 / 60.0).roundToInt()
		world.step(scaledDelta, iterations, iterations)

	}

	override val eventManager = PlanckEventManager(world)

	override fun createBody(position: Double2, entity: Entity, shape: IShape) = shape.build(PlanckShapeBuilder(position, entity, world))
}