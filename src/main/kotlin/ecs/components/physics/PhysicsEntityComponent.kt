package ecs.components.physics

import engine.physics.engines.PhysicsEngine
import engine.component.IMessyComponent
import jslib.Matter

class PhysicsEntityComponent(val body: Matter.Body) : IMessyComponent {
	override fun cleanup() {
		Matter.World.remove(PhysicsEngine.world, body)
	}

}