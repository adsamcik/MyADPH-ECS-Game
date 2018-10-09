package engine.entity

import engine.component.IComponent
import engine.system.SystemManager
import kotlin.reflect.KClass

object EntityManager {
    private var nextId = 0
    private val entities = mutableMapOf<Entity, MutableMap<KClass<out IComponent>, IComponent>>()

    fun createEntity(vararg components: IComponent) = createEntity(components.toList())

    fun createEntity(components: Array<IComponent>) = createEntity(components.toList())


    fun createEntity(components: Collection<IComponent>): Entity {
        val entity = Entity(nextId++)

        val componentMap = mutableMapOf<KClass<out IComponent>, IComponent>()

        components.forEach { componentMap[it::class] = it }

        entities[entity] = componentMap
        SystemManager.onEntityChanged(entity)
        return entity
    }

    fun removeEntity(entity: Entity) {
        entities.remove(entity)
    }

    fun getComponents(entity: Entity) = entities[entity]
            ?: throw RuntimeException("entity $entity is either destroyed or does not exist")

    fun removeComponent(entity: Entity, component: IComponent) {
        val components = getComponents(entity)

        if (components.remove(component::class) == null)
            throw RuntimeException("entity $entity does not have component of type ${component::class.js.name}")
    }

    fun hasComponent(entity: Entity, component: KClass<out IComponent>): Boolean = getComponents(entity).containsKey(component)

    fun <T> getComponent(entity: Entity, componentClass: KClass<out T>): T where T : IComponent {
        val components = getComponents(entity)
        return components[componentClass].unsafeCast<T>()
    }

    fun addComponent(entity: Entity, component: IComponent) {
        val components = getComponents(entity)
        if (components.containsKey(component::class))
            throw RuntimeException("component ${component::class.js.name} is already added")

        components[component::class] = component

        SystemManager.onEntityChanged(entity)
    }
}