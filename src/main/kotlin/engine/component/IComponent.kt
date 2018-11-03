package engine.component

import engine.interfaces.IMemento

interface IComponent

interface IMessyComponent : IComponent {
	fun cleanup()
}

interface IMementoComponent : IComponent {
	fun save(): IMemento
	fun restore(memento: IMemento)
}