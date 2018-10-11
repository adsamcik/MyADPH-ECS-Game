package engine.system

import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import utility.ECInclusionNode
import utility.ValueNodeIterator
import kotlin.reflect.KClass

object SystemManager {
	private val systems = mutableListOf<SystemData>()

	fun registerSystems(vararg systems: Pair<IBaseSystem, Int>) {
		systems.forEach { pair ->
			if (SystemManager.systems.any { it.system::class == pair.first::class })
				throw RuntimeException("system ${pair.first::class.js.name} is already registered")

			val system = pair.first

			when (system) {
				is ISystem -> registerSystem(system, pair.second)
				is IComponentSystem -> registerSystem(system, pair.second)
			}
		}

		SystemManager.systems.sortBy { it.priority }
	}

	private fun registerSystem(system: ISystem, priority: Int) {
		systems.add(EntitySystemData(system, priority))
	}

	private fun registerSystem(system: IComponentSystem, priority: Int) {
		systems.add(EntityComponentSystemData(system, priority))
	}

	fun unregisterSystem(system: IBaseSystem) {
		unregisterSystem(system::class)
	}

	fun unregisterSystem(systemType: KClass<out IBaseSystem>) {
		val indexOf = systems.indexOfFirst { it.system::class == systemType }
		if (indexOf < 0)
			throw RuntimeException("system ${systemType.simpleName} is not registered")

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
			it.update(deltaTime)
		}
	}
}

internal abstract class SystemData(val priority: Int) {
	private val entityCollection: MutableCollection<Entity> = mutableListOf()

	val entities: Collection<Entity>
		get() = entityCollection

	abstract val system: IBaseSystem

	fun onEntityRemoved(entity: Entity) {
		entityCollection.remove(entity)
	}

	/**
	 *
	 * Should be called when onEntityChanged is called
	 * @return Returns true if entity was added
	 */
	protected fun onEntityChangedInternal(entity: Entity): Boolean {
		val meetsRequirements = system.requirements.evaluate(entity)

		if (entityCollection.contains(entity)) {
			if (!meetsRequirements)
				entityCollection.remove(entity)
		} else {
			if (meetsRequirements) {
				entityCollection.add(entity)
				return true
			}
		}

		return false
	}

	open fun onEntityChanged(entity: Entity) {
		onEntityChangedInternal(entity)
	}

	abstract fun update(deltaTime: Double)
}

internal class EntitySystemData(private val _system: ISystem, priority: Int) : SystemData(priority) {
	override val system: IBaseSystem
		get() = _system

	override fun update(deltaTime: Double) {
		if (entities.isNotEmpty())
			_system.update(deltaTime, entities)
	}
}

internal class EntityComponentSystemData(private val _system: IComponentSystem, priority: Int) : SystemData(priority) {
	override val system: IBaseSystem
		get() = _system

	private val components: MutableMap<KClass<out IComponent>, MutableList<IComponent>> = mutableMapOf()

	private fun getComponentCollection(type: KClass<out IComponent>): MutableCollection<IComponent> {
		var collection = this.components[type]
		if (collection == null) {
			collection = mutableListOf()
			this.components[type] = collection
		}
		return collection
	}


	override fun onEntityChanged(entity: Entity) {
		val added = onEntityChangedInternal(entity)

		if (added) {
			ValueNodeIterator<Entity, KClass<out IComponent>>(system.requirements).forEach {
				if (it is ECInclusionNode) {
					if (EntityManager.hasComponent(entity, it.value))
						getComponentCollection(it.value).add(entity.getComponent(it.value))
				}
			}
		}
	}

	override fun update(deltaTime: Double) {
		if (entities.isNotEmpty())
			_system.update(deltaTime, entities, this.components)
	}
}