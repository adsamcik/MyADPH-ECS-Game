package engine.physics.engines

import ecs.components.physics.MatterPhysicsEngineComponent
import engine.entity.EntityManager
import engine.physics.events.PhysicsEventManager
import jslib.Matter

class MatterPhysicsEngine : PhysicsEngine() {
	val engine: Matter.Engine = Matter.Engine.create()

	val world: Matter.World
		get() = engine.world


	override val eventManager: PhysicsEventManager

	init {
		engine.enableSleeping = true
		EntityManager.createEntity(MatterPhysicsEngineComponent(engine))
		eventManager = engine.physics.PhysicsEventManager(engine)
	}
}