package engine

import Matter
import ecs.components.PhysicsEngineComponent
import engine.entity.EntityManager
import engine.events.PhysicsEventManager

object PhysicsEngine {
	val engine: Matter.Engine = Matter.Engine.create()

	val world: Matter.World
		get() = engine.world

	val eventManager: PhysicsEventManager

	const val DEBUG = false

	init {
		engine.enableSleeping = true
		EntityManager.createEntity(PhysicsEngineComponent(engine))
		eventManager = PhysicsEventManager(engine)
	}
}