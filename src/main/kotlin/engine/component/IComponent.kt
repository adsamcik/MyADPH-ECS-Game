package engine.component

interface IComponent

interface IMessyComponent : IComponent {
	fun cleanup()
}