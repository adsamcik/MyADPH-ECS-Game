package engine.physics.engines

import engine.physics.events.PhysicsEventManager

abstract class PhysicsEngine {
	abstract val eventManager: PhysicsEventManager
}