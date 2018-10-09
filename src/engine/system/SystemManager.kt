package engine.system

import engine.entity.Entity

object SystemManager {
    private val systems = mutableListOf<SystemData>()

    fun registerSystem(system: ISystem, priority: Int = 0) {
        if (systems.any { it.system::class == system::class })
            throw RuntimeException("system ${system::class.js.name} is already registered")

        systems.add(SystemData(system, mutableListOf(), priority))
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

    fun onEntityChanged(entity: Entity) {
        systems.forEach {
            it.onEntityChanged(entity)
        }
    }

    fun update(deltaTime: Double) {
        systems.forEach {
            if (it.entities.isNotEmpty())
                it.system.update(deltaTime, it.entities)
        }
    }
}

data class SystemData(val system: ISystem, private val entityCollection: MutableCollection<Entity>, val priority: Int) {
    val entities: Collection<Entity>
        get() = entityCollection

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