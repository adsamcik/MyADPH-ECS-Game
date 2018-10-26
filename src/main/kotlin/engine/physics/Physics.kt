package engine.physics

import engine.physics.engines.MatterPhysicsEngine
import engine.physics.engines.PhysicsEngine
import engine.physics.engines.PlanckPhysicsEngine
import kotlin.browser.window

object Physics {
	const val DEBUG = false

	val engineType = EngineType.Matterjs

	val engine: PhysicsEngine

	const val timeStep = 20

	init {
		engine = when (engineType) {
			EngineType.Matterjs -> MatterPhysicsEngine()
			EngineType.Planckjs -> PlanckPhysicsEngine()
		}

		window.setInterval(this::update, timeStep)
	}

	private fun update() {
		engine.update(timeStep)
	}

	enum class EngineType {
		Matterjs,
		Planckjs
	}

}