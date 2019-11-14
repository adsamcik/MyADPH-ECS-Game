package ecs.components.health

import engine.component.IMementoComponent
import engine.interfaces.IMemento
import kotlinx.serialization.Serializable

@Serializable
data class DamageComponent(var value: Double) : IMementoComponent {
	override fun save() = Memento(value)

	override fun restore(memento: IMemento) {
		memento as Memento
		value = memento.damage
	}

	data class Memento(val damage: Double) : IMemento

}