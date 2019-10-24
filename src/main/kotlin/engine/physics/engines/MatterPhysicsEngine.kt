package engine.physics.engines

import engine.entity.Entity
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.shapes.builders.MatterShapeBuilder
import engine.physics.events.MatterEventManager
import engine.physics.events.PhysicsEventManager
import general.Double2
import definition.jslib.Matter

class MatterPhysicsEngine : PhysicsEngine() {
	val engine: Matter.Engine = Matter.Engine.create()

	val world: Matter.World
		get() = engine.world

	override val eventManager: PhysicsEventManager = MatterEventManager(engine)


	init {
		engine.enableSleeping = true
		engine.world.gravity.scale *= MATTER_SCALE / 5
	}

	override fun createBody(position: Double2, entity: Entity, shape: IShape) = shape.build(MatterShapeBuilder(position, entity, world))

	override fun update(delta: Double) {
		Matter.Engine.update(engine, delta * 1000.0)
	}

	companion object {
		const val MATTER_SCALE = 100.0
	}

}