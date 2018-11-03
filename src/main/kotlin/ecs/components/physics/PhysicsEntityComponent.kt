package ecs.components.physics

import engine.component.IMementoComponent
import engine.component.IMessyComponent
import engine.interfaces.IMemento
import engine.physics.BodyBuilder
import engine.physics.IShape
import engine.physics.bodies.IBody

class PhysicsEntityComponent(var body: IBody, var shape: IShape) : IMessyComponent, IMementoComponent {
	override fun save() = Memento(body.save(), shape.duplicate())

	override fun restore(memento: IMemento) {
		if (memento !is PhysicsEntityComponent.Memento)
			throw IllegalArgumentException("Expected memento of type ${PhysicsEntityComponent.Memento::class} but got ${memento::class}")

		if (memento.shape != shape) {
			body.destroy()
			body = BodyBuilder().apply {
				shape = memento.shape
			}.buildBody(body.entity)
		}

		body.restore(memento.bodyMemento)

	}

	override fun cleanup() {
		body.destroy()
	}


	data class Memento(val bodyMemento: IMemento, val shape: IShape) : IMemento

}