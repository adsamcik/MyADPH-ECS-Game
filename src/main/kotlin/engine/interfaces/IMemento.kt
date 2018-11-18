package engine.interfaces

interface IMemento

//null object pattern
object EmptyMemento : IMemento

//PATTERN Memento
interface IMementoClass {
	fun save(): IMemento
	fun restore(memento: IMemento)
}