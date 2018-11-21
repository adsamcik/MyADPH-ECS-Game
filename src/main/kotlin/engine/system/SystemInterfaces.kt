package engine.system

import engine.component.IComponent
import engine.entity.Entity
import engine.system.requirements.INode
import kotlin.reflect.KClass

interface IBaseSystem {
	val requirements: INode<Entity>
}

interface ISystem : IBaseSystem {
	fun update(deltaTime: Double, entities: Collection<Entity>)
}

typealias EntityComponentCollection = Map<KClass<out IComponent>, List<IComponent>>

interface IComponentSystem : IBaseSystem {
	fun update(deltaTime: Double, entities: Collection<Entity>, components: EntityComponentCollection)
}


//there to create the same structure as Systems have
interface IBaseEventSystem

interface IEventSystem : IBaseEventSystem