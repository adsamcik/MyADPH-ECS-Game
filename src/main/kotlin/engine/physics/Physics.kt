package engine.physics

import ecs.components.physics.PhysicsUpdateComponent
import engine.entity.EntityManager
import engine.physics.engines.MatterPhysicsEngine
import engine.physics.engines.PhysicsEngine
import engine.physics.engines.PlanckPhysicsEngine

object Physics {
	const val DEBUG = false

	val engineType = EngineType.Matterjs

	val engine: PhysicsEngine

	init {
		engine = when (engineType) {
			EngineType.Matterjs -> MatterPhysicsEngine()
			EngineType.Planckjs -> PlanckPhysicsEngine()
		}

		EntityManager.createEntity {
			addComponent(PhysicsUpdateComponent(engine))
		}
	}

	enum class EngineType {
		Matterjs,
		Planckjs
	}

}