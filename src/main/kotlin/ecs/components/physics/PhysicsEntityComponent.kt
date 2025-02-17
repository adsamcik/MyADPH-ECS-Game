package ecs.components.physics

import engine.component.IGeneratedComponent
import engine.component.IMessyComponent
import engine.physics.bodies.IBody
import kotlinx.serialization.Serializable

class PhysicsEntityComponent(val body: IBody) : IMessyComponent, IGeneratedComponent {
	override fun cleanup() {
		body.destroy()
	}
}