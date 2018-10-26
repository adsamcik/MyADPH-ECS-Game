package ecs.components.physics

import engine.component.IMessyComponent
import engine.physics.bodies.IBody

class PhysicsEntityComponent(val body: IBody) : IMessyComponent {
	override fun cleanup() {
		body.destroy()
	}

}