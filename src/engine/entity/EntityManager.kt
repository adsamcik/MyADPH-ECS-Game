package engine.entity

import engine.component.IComponent
import engine.component.IMessyComponent
import engine.system.SystemData
import engine.system.SystemManager
import kotlin.reflect.KClass

//todo add EntityOperation so operations can be more easily done without triggering early onEntityChanged
object EntityManager {
	private var nextId = 0
	private val entityData = mutableMapOf<Entity, MutableMap<KClass<out IComponent>, IComponent>>()

	val entityCount
		get() = entityData.size

	fun createEntity(vararg components: IComponent) = createEntity(components.toList())

	fun createEntity(components: Array<IComponent>) = createEntity(components.toList())


	fun createEntity(components: Collection<IComponent>): Entity {
		val entity = Entity(nextId++)
		setNewEntityComponents(entity, components)
		return entity
	}

	fun createEntity(componentBuilder: EntityComponentsBuilder.(Entity) -> Unit): Entity {
		val entity = Entity(nextId++)
		val builder = EntityComponentsBuilder()
		componentBuilder(builder, entity)
		setNewEntityComponents(entity, builder.components)
		return entity
	}

	private fun setNewEntityComponents(entity: Entity,  components: Collection<IComponent>) {
		val componentMap = mutableMapOf<KClass<out IComponent>, IComponent>()

		components.forEach { componentMap[it::class] = it }

		entityData[entity] = componentMap
		SystemManager.onEntityChanged(entity)
	}

	fun removeEntity(entity: Entity) {
		val data = entityData.remove(entity)
		SystemManager.onEntityRemoved(entity)
		data!!.forEach {
			val item = it.value
			if (item is IMessyComponent)
				item.cleanup()
		}
	}

	fun getComponents(entity: Entity): MutableMap<KClass<out IComponent>, IComponent> = entityData[entity]
			?: throw Error("entity $entity is either destroyed or does not exist")


	fun setComponent(entity: Entity, component: IComponent) {
		val components = getComponents(entity)
		val currentComponent = components[component::class]
				?: throw NullPointerException("Set requires that the component is already set, use add instead")

		if (currentComponent is IMessyComponent)
			currentComponent.cleanup()

		components[component::class] = component
		onEntityChanged(entity)
	}

	fun setComponents(entity: Entity, vararg components: IComponent) {
		val entityComponents = getComponents(entity)
		components.forEach { entityComponents[it::class] = it }
	}

	fun removeComponent(entity: Entity, component: IComponent) {
		removeComponent(entity, component::class)
	}

	fun removeComponent(entity: Entity, componentType: KClass<out IComponent>) {
		val components = getComponents(entity)

		removeComponent(components, componentType)

		onEntityChanged(entity)
	}

	fun removeComponents(entity: Entity, vararg componentType: KClass<out IComponent>) {
		val components = getComponents(entity)

		componentType.forEach {
			removeComponent(components, it)
		}

		onEntityChanged(entity)
	}

	private fun removeComponent(components: MutableMap<KClass<out IComponent>, IComponent>, componentType: KClass<out IComponent>) {
		val component = components.remove(componentType)
				?: throw RuntimeException("entity does not have components of type ${componentType.simpleName}")

		if (component is IMessyComponent)
			component.cleanup()
	}

	fun hasComponent(entity: Entity, component: KClass<out IComponent>): Boolean = getComponents(entity).containsKey(component)

	fun hasComponentType(entity: Entity, componentType: KClass<out IComponent>): Boolean = getComponents(entity).any { componentType.isInstance(it) }

	fun <T> getComponent(entity: Entity, componentClass: KClass<out T>): T where T : IComponent {
		val components = getComponents(entity)
		return components[componentClass].unsafeCast<T>()
	}

	fun addComponent(entity: Entity, component: IComponent) {
		val components = getComponents(entity)
		if (components.containsKey(component::class))
			throw RuntimeException("components ${component::class.simpleName} is already added")

		components[component::class] = component

		onEntityChanged(entity)
	}

	fun addComponents(entity: Entity, vararg componentToAdd: IComponent) {
		val components = getComponents(entity)

		componentToAdd.forEach {
			if (components.containsKey(it::class))
				throw RuntimeException("components ${it::class.simpleName} is already added")

			components[it::class] = it
		}

		onEntityChanged(entity)
	}

	private fun onEntityChanged(entity: Entity) {
		SystemManager.onEntityChanged(entity)
	}

	internal fun onSystemAdded(systemData: SystemData) {
		entityData.forEach {
			systemData.onEntityChanged(it.key)
		}
	}
}