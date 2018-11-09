package ecs.components.physics

import engine.component.IMementoComponent
import engine.component.IMessyComponent
import engine.interfaces.IMemento
import engine.physics.bodies.IBody

class PhysicsEntityComponent(val body: IBody) : IMessyComponent, IMementoComponent {
	override fun save() = body.save()

	override fun restore(memento: IMemento) {
		when (memento) {
			is IBody.Memento -> {
				body.restore(memento)
			}
			else -> throw IllegalArgumentException("Memento of type ${memento::class} not supported")
		}
	}


	override fun cleanup() {
		body.destroy()
	}
}