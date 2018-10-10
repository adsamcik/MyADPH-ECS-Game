package engine.system

import engine.entity.Entity
import engine.entity.EntityManager

object SystemManager {
    private val systems = mutableListOf<SystemData>()

    fun registerSystems(vararg systems: Pair<ISystem, Int>) {
        systems.forEach { pair ->
            if (SystemManager.systems.any { it.system::class == pair.first::class })
                throw RuntimeException("system ${pair.first::class.js.name} is already registered")

            SystemManager.systems.add(SystemData(pair.first, pair.second, mutableListOf()))
        }

        SystemManager.systems.sortBy { it.priority }
    }

    fun registerSystem(system: ISystem, priority: Int = 0) {
        if (systems.any { it.system::class == system::class })
            throw RuntimeException("system ${system::class.js.name} is already registered")

        systems.add(SystemData(system, priority, mutableListOf()))
        systems.sortBy { it.priority }
    }

    fun unregisterSystem(system: ISystem) {
        unregisterSystem(system::class.js)
    }

    fun unregisterSystem(systemType: JsClass<out ISystem>) {
        val indexOf = systems.indexOfFirst { it.system::class.js == systemType }
        if (indexOf < 0)
            throw RuntimeException("system ${systemType.name} is not registered")

        systems.removeAt(indexOf)
    }

    internal fun onEntityChanged(entity: Entity) {
        systems.forEach {
            it.onEntityChanged(entity)
        }
    }

    internal fun onEntityRemoved(entity: Entity) {
        systems.forEach {
            it.onEntityRemoved(entity)
        }
    }

    internal fun update(deltaTime: Double) {
        systems.forEach {
            if (it.entities.isNotEmpty())
                it.system.update(deltaTime, it.entities)
        }
    }
}

internal data class SystemData(val system: ISystem, val priority: Int, private val entityCollection: MutableCollection<Entity> = mutableListOf()) {
    val entities: Collection<Entity>
        get() = entityCollection

    fun onEntityRemoved(entity: Entity) {
        entityCollection.remove(entity)
    }

    fun onEntityChanged(entity: Entity) {
        val meetsRequirements = system.requirements.all { it.isMet(entity) }

        if (entityCollection.contains(entity)) {
            if (!meetsRequirements)
                entityCollection.remove(entity)
        } else {
            if (meetsRequirements)
                entityCollection.add(entity)
        }
    }
}