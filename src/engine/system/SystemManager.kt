package engine.system

import engine.entity.Entity

object SystemManager {
    private val systems = mutableMapOf<String, SystemData>()

    fun registerSystem(system: ISystem) {
        if (systems[system::class.js.name] != null)
            throw RuntimeException("system ${system::class.js.name} is already registered")

        systems[system::class.js.name] = SystemData(system, mutableListOf())
    }

    fun unregisterSystem(system: ISystem) {
        unregisterSystem(system::class.js)
    }

    fun unregisterSystem(systemType: JsClass<out ISystem>) {
        if (systems[systemType.name] == null)
            throw RuntimeException("system ${systemType.name} is not registered")

        systems.remove(systemType.name)
    }

    fun onEntityChanged(entity: Entity) {
        systems.forEach {
            it.value.onEntityChanged(entity)
        }
    }

    fun update(deltaTime: Double) {
        systems.forEach {
            if (it.value.entities.isNotEmpty())
                it.value.system.update(deltaTime, it.value.entities)
        }
    }
}

data class SystemData(val system: ISystem, private val entityCollection: MutableCollection<Entity>) {
    val entities: Collection<Entity>
        get() = entityCollection

    fun onEntityChanged(entity: Entity) {
        val meetsRequirements = system.componentSpecification().all { it.isMet(entity) }

        if (entityCollection.contains(entity)) {
            if (!meetsRequirements)
                entityCollection.remove(entity)
        } else {
            if (meetsRequirements)
                entityCollection.add(entity)
        }
    }
}