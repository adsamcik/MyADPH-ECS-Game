package engine.component

import engine.interfaces.IMementoClass
import kotlinx.serialization.Serializable

interface IComponent

interface IMessyComponent : IComponent {
	fun cleanup()
}

interface IGeneratedComponent : IComponent

interface IMementoComponent : IComponent, IMementoClass

@Serializable
data class ComponentWrapper(val c: IComponent)