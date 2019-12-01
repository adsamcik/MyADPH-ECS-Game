package engine.serialization

import ecs.components.*
import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import engine.component.ComponentWrapper
import engine.component.IComponent
import engine.component.IGeneratedComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.bodies.builder.BodyBuilder
import engine.physics.bodies.builder.IBodyBuilder
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.shapes.Polygon
import engine.physics.bodies.shapes.Rectangle
import game.editor.component.CheckpointDefinitionComponent
import game.editor.component.PlayerDefinitionComponent
import game.levels.EntityCreator
import game.modifiers.IModifierFactory
import game.modifiers.MaxEnergyModifierFactory
import game.modifiers.ShapeModifierFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.stringify

object EntitySerializer {
	@Serializable
	data class EntityData(val entity: Int, val components: List<ComponentWrapper>) {
		constructor(entity: Entity, components: Collection<IComponent>) : this(
			entity.id,
			components.map { ComponentWrapper(it) }
		)
	}

	private val messageModule = SerializersModule {
		polymorphic(IShape::class) {
			Circle::class with Circle.serializer()
			Rectangle::class with Rectangle.serializer()
			Polygon::class with Polygon.serializer()
		}

		polymorphic(IComponent::class) {
			BodyComponent::class with BodyComponent.serializer()
			EnergyComponent::class with EnergyComponent.serializer()
			HealthComponent::class with HealthComponent.serializer()
			LifeTimeComponent::class with LifeTimeComponent.serializer()
			RotateMeComponent::class with RotateMeComponent.serializer()
			PlayerDefinitionComponent::class with PlayerDefinitionComponent.serializer()
			CheckpointDefinitionComponent::class with CheckpointDefinitionComponent.serializer()
			AccelerationComponent::class with AccelerationComponent.serializer()
			DamageComponent::class with DamageComponent.serializer()
			ModifierSpreaderComponent::class with ModifierSpreaderComponent.serializer()
		}

		polymorphic(IModifierFactory::class) {
			MaxEnergyModifierFactory::class with MaxEnergyModifierFactory.serializer()
			ShapeModifierFactory::class with ShapeModifierFactory.serializer()
		}

		polymorphic(IBodyBuilder::class) {
			BodyBuilder::class with BodyBuilder.serializer()
			MutableBodyBuilder::class with MutableBodyBuilder.serializer()
		}
	}

	fun deserialize(json: String): List<Entity> {
		val parser = Json(configuration = JsonConfiguration(prettyPrint = false), context = messageModule)
		val list = parser.parse(EntityData.serializer().list, json)

		if (list.isEmpty()) return emptyList()

		val entityList = mutableListOf<Entity>()
		list.forEach {
			if (it.components.isEmpty()) return@forEach

			val components = it.components.map { wrapper -> wrapper.c }.toMutableList()

			val bodyComponentIndex = components.indexOfFirst { component -> component::class == BodyComponent::class }

			val entity: Entity
			if (bodyComponentIndex >= 0) {
				val bodyComponent = components.removeAt(bodyComponentIndex) as BodyComponent
				val isPlayer = components.any { item -> item::class == PlayerDefinitionComponent::class }
				entity = EntityCreator.createWithBody {
					this.isPlayer = isPlayer
					bodyBuilder = bodyComponent.value
					components.forEach { addComponent { it } }
				}

				EntityManager.tryGetComponent<ModifierSpreaderComponent>(entity)?.run {
					factory.setSourceEntity(entity)
				}
			} else {
				entity = EntityManager.createEntity(components)
			}

			entityList.add(entity)
		}
		return entityList
	}

	fun serialize(): String = serialize(EntityManager.serializationEntityData)

	fun serializeEntity(entity: Entity): String {
		return serialize(listOf(EntityManager.getEntitySerializationData(entity)))
	}

	fun serialize(entityDataList: List<EntityData>): String {
		val json = Json(configuration = JsonConfiguration(prettyPrint = false), context = messageModule)
		return json.stringify(entityDataList)
	}

}