package engine.entity

import engine.component.IComponent
import engine.system.SystemManager
import kotlin.reflect.KClass

object EntityManager {
    private var nextId = 0
    private val entities = mutableMapOf<Entity, HashSet<IComponent>>()

    fun createEntity(componentCount: Int): Entity = createEntity(HashSet(componentCount))

    fun createEntity(components: Array<IComponent>) = createEntity(components.toHashSet())

    fun createEntity(vararg components: IComponent) = createEntity(components.toHashSet())

    fun createEntity(components: HashSet<IComponent>): Entity {
        val entity = Entity(nextId++)
        entities[entity] = components
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

        if (!components.remove(component))
            throw RuntimeException("entity $entity does not have component of type ${component::class.js.name}")
    }

    fun hasComponent(entity: Entity, component: KClass<out IComponent>): Boolean = getComponents(entity).any { it::class == component }

    fun <T> getComponent(entity: Entity, component: KClass<out T>) : T where T : IComponent {
        val components = getComponents(entity)
        return components.find { it::class == component }.unsafeCast<T>()
    }

    fun addComponent(entity: Entity, component: IComponent) {
        if(!getComponents(entity).add(component))
            throw RuntimeException("component ${component::class.js.name} is already added")

        SystemManager.onEntityChanged(entity)
    }
}