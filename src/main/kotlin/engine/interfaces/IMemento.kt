package engine.interfaces

interface IMemento

interface IMementoClass {
	fun save(): IMemento
	fun restore(memento: IMemento)
}