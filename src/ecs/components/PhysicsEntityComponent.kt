package ecs.components

import Matter
import engine.PhysicsEngine
import engine.component.IMessyComponent

class PhysicsEntityComponent(val body: Matter.Body) : IMessyComponent {
	override fun cleanup() {
		Matter.World.remove(PhysicsEngine.world, body)
	}

}