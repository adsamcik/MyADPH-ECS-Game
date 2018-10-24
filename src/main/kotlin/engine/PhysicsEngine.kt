package engine

import ecs.components.PhysicsEngineComponent
import engine.entity.EntityManager
import engine.events.PhysicsEventManager
import jslib.Matter
import kotlin.browser.window

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

		world.bounds.min.x = 0.0
		world.bounds.min.y = 0.0
		world.bounds.max.x = window.innerWidth.toDouble()
		world.bounds.max.y = window.innerHeight.toDouble()
	}
}