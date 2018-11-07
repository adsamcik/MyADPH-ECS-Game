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
		when (memento) {
			is Memento -> {
				restoreShape(memento)
				body.restore(memento.bodyMemento)
			}
			is IBody.Memento -> {
				body.restore(memento)
			}
			else -> throw IllegalArgumentException("Memento of type ${memento::class} not supported")
		}
	}

	fun saveProperties() = body.save()

	fun restoreShape(memento: IMemento) = restoreShape(memento as Memento)

	private fun restoreShape(memento: Memento) {
		if (memento.shape != shape) {
			val saveState = body.save()
			body.destroy()
			body = BodyBuilder().apply {
				shape = memento.shape
			}.buildBody(body.entity)

			body.restore(saveState)
			shape = memento.shape
		}
	}

	override fun cleanup() {
		body.destroy()
	}


	data class Memento(val bodyMemento: IMemento, val shape: IShape) : IMemento

}