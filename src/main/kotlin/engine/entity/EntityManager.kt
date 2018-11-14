package engine.entity

import engine.component.IComponent
import engine.component.IMessyComponent
import engine.system.SystemData
import engine.system.SystemManager
import kotlin.reflect.KClass

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

	private fun setNewEntityComponents(entity: Entity, components: Collection<IComponent>) {
		val componentMap = mutableMapOf<KClass<out IComponent>, IComponent>()

		components.forEach { componentMap[it::class] = it }

		entityData[entity] = componentMap
		onEntityChanged(entity)
	}

	fun removeEntitySafe(entity: Entity) {
		val data = entityData[entity]
		if (data != null)
			removeEntity(entity, data)
	}

	fun removeEntity(entity: Entity) = removeEntity(entity, entityData[entity]!!)

	private fun removeEntity(entity: Entity, components: Map<KClass<out IComponent>, IComponent>) {
		components.forEach {
			val item = it.value
			if (item is IMessyComponent)
				item.cleanup()
		}

		SystemManager.onEntityRemoved(entity)

		//Must be removed as last so it does not break possible cleanup callbacks that require its data
		entityData.remove(entity)
	}

	private fun getComponents(entity: Entity): MutableMap<KClass<out IComponent>, IComponent> = entityData[entity]
		?: throw Error("entity $entity is either destroyed or does not exist")


	fun setComponent(entity: Entity, component: IComponent) {
		setComponent(component, getComponents(entity))
	}

	fun setComponents(entity: Entity, vararg components: IComponent) {
		val entityComponents = getComponents(entity)
		components.forEach {
			setComponent(it, entityComponents)
		}
	}

	private fun setComponent(component: IComponent, entityComponents: MutableMap<KClass<out IComponent>, IComponent>) {
		val currentComponent = entityComponents[component::class]
			?: throw NullPointerException("Set requires that the component is already set, use add instead")

		if (currentComponent is IMessyComponent)
			currentComponent.cleanup()

		entityComponents[component::class] = component
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

	private fun removeComponent(
		components: MutableMap<KClass<out IComponent>, IComponent>,
		componentType: KClass<out IComponent>
	) {
		val component = components.remove(componentType)
			?: throw RuntimeException("entity does not have components of type ${componentType.simpleName}")

		if (component is IMessyComponent)
			component.cleanup()
	}

	fun hasComponent(entity: Entity, component: KClass<out IComponent>): Boolean =
		getComponents(entity).containsKey(component)

	fun <T> getComponent(entity: Entity, componentClass: KClass<out T>): T where T : IComponent {
		val components = getComponents(entity)
		return components[componentClass].unsafeCast<T>()
	}

	inline fun <reified T : IComponent> getComponent(entity: Entity): T = getComponent(entity, T::class)

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