package engine.physics

import ecs.components.physics.MatterPhysicsEngineComponent
import engine.entity.EntityManager
import engine.physics.engines.PhysicsEngine
import engine.physics.engines.MatterPhysicsEngine
import engine.physics.engines.PlanckPhysicsEngine
import engine.physics.events.PhysicsEventManager

object Physics {
	val eventManager: PhysicsEventManager

	const val DEBUG = false

	val engineType = EngineType.Planckjs

	val engine: PhysicsEngine

	init {
		engine = when(engineType) {
			EngineType.Matterjs -> MatterPhysicsEngine()
			EngineType.Planckjs -> PlanckPhysicsEngine()
		}
	}

	private fun initializeMatterEngine() {
		EntityManager.createEntity(MatterPhysicsEngineComponent(engine))
		eventManager = engine.physics.PhysicsEventManager(engine.physics.PhysicsEngine.engine)
	}

	private fun initializePlanckEngine() {

	}

	enum class EngineType {
		Matterjs,
		Planckjs
	}

}