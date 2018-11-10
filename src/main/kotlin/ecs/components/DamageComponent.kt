package ecs.components

import engine.component.IMementoComponent
import engine.interfaces.IMemento

data class DamageComponent(var value: Double) : IMementoComponent {
	override fun save() = Memento(value)

	override fun restore(memento: IMemento) {
		memento as Memento
		value = memento.damage
	}

	data class Memento(val damage: Double) : IMemento

}