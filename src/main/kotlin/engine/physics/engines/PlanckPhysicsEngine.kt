package engine.physics.engines

import engine.entity.Entity
import engine.physics.IShape
import engine.physics.bodies.PlanckBody
import engine.physics.events.PlanckEventManager
import jslib.planck
import utility.Double2
import kotlin.math.roundToInt

class PlanckPhysicsEngine : PhysicsEngine() {

	val world = planck.World()

	init {
		world.setGravity(planck.Vec2(0, 9.8))
	}

	override fun update(delta: Double) {
		val scaledDelta = delta * 10
		val iterations = (delta / 1.0 / 60.0).roundToInt()
		world.step(scaledDelta, iterations, iterations)

	}

	override val eventManager = PlanckEventManager(world)

	override fun createBody(position: Double2, entity: Entity, shape: IShape) = PlanckBody(shape, position, entity, world)
}