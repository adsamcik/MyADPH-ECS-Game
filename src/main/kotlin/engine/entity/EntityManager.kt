package engine.entity

import debug.Debug
import debug.DebugLevel
import ecs.components.*
import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.physics.PhysicsKinematicEntityComponent
import ecs.components.triggers.CheckpointComponent
import engine.component.ComponentWrapper
import engine.component.IComponent
import engine.component.IGeneratedComponent
import engine.component.IMessyComponent
import engine.physics.bodies.builder.BodyBuilder
import engine.physics.bodies.builder.IBodyBuilder
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.shapes.Polygon
import engine.physics.bodies.shapes.Rectangle
import engine.serialization.EntitySerializer
import engine.serialization.IEntitySerializationProvider
import engine.system.SystemData
import engine.system.SystemManager
import game.editor.component.CheckpointDefinitionComponent
import game.editor.component.PlayerDefinitionComponent
import game.levels.EntityCreator
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

object EntityManager : IEntitySerializationProvider {
	private var nextId = 0
	private val entityData = mutableMapOf<Entity, MutableMap<KClass<out IComponent>, IComponent>>()

	val entityCount
		get() = entityData.size

	fun createEntity(vararg components: IComponent) = createEntity(components.toList())

	fun createEntity(components: Array<IComponent>) = createEntity(components.toList())


	fun createEntity(components: Collection<IComponent>): Entity {
		val entity = Entity(nextId++)
		addNewEntity(entity, components)
		return entity
	}

	fun createEntity(componentBuilder: EntityComponentsBuilder.(Entity) -> Unit): Entity {
		val entity = Entity(nextId++)
		val builder = EntityComponentsBuilder()
		componentBuilder(builder, entity)
		addNewEntity(entity, builder.components)
		return entity
	}

	private fun addNewEntity(entity: Entity, components: Collection<IComponent>) {
		val componentMap = mutableMapOf<KClass<out IComponent>, IComponent>()

		components.forEach { componentMap[it::class] = it }

		entityData[entity] = componentMap
		onEntityChanged(entity)
	}

	fun removeEntitySafe(entity: Entity) {
		val data = entityData[entity]
		if (data != null) {
			removeEntity(entity, data)
		}
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

	fun getComponentsList(entity: Entity): List<IComponent> = getComponents(entity).map { it.value }

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

	inline fun <reified T> tryGetComponent(entity: Entity): T? where T : IComponent {
		return tryGetComponent(entity, T::class)
	}

	fun <T> tryGetComponent(entity: Entity, componentClass: KClass<out T>): T? where T : IComponent {
		return getComponents(entity)[componentClass].unsafeCast<T?>()
	}

	fun <T> getComponent(entity: Entity, componentClass: KClass<out T>): T where T : IComponent {
		val components = getComponents(entity)
		if (components[componentClass] == null) {
			Debug.log(
				DebugLevel.IMPORTANT,
				"Component ${componentClass.simpleName} not found on entity $entity",
				components.values.toTypedArray()
			)
		}
		return components[componentClass].unsafeCast<T>()
	}

	fun getEntityListByComponent(component: KClass<out IComponent>) =
		entityData.keys.filter { entity -> hasComponent(entity, component) }

	inline fun <reified T : IComponent> getComponent(entity: Entity): T = getComponent(entity, T::class)

	fun getEntityByComponent(component: IComponent): Entity {
		val type = component::class
		entityData.forEach { mapEntry ->
			val find = mapEntry.value[type]
			if (find == component) {
				return mapEntry.key
			}
		}
		Debug.log(DebugLevel.CRITICAL, "Component does not belong to an entity.", component)
		throw IllegalArgumentException("Component does not belong to an entity.")
	}

	fun addComponent(entity: Entity, component: IComponent) {
		val components = getComponents(entity)
		if (components.containsKey(component::class)) {
			console.log(components.values.toTypedArray(), component)
			throw RuntimeException("components ${component::class.simpleName} is already added")
		}

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


	//PATTERN observer?
	private fun onEntityChanged(entity: Entity) {
		SystemManager.onEntityChanged(entity)
	}

	internal fun onSystemAdded(systemData: SystemData) {
		entityData.forEach {
			systemData.onEntityChanged(it.key)
		}
	}

	//Serialization
	override val serializationEntityData: List<EntitySerializer.EntityData>
		get() = entityData.map { serializeEntityData(it.key, it.value.values) }

	override fun getEntitySerializationData(entity: Entity): EntitySerializer.EntityData {
		return serializeEntityData(entity, getComponentsList(entity))
	}

	private fun serializeEntityData(entity: Entity, components: Collection<IComponent>): EntitySerializer.EntityData {
		return EntitySerializer.EntityData(entity, components.filter { it !is IGeneratedComponent })
	}
}