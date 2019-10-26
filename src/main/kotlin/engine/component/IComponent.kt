package engine.component

import engine.interfaces.IMementoClass

interface IComponent

interface IMessyComponent : IComponent {
	fun cleanup()
}

interface IGeneratedComponent : IComponent

interface IMementoComponent : IComponent, IMementoClass