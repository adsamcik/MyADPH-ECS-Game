package game.levels

import ecs.components.PlayerComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.bodies.BodyMotionType

abstract class Level(val id: String) {
	private val staticEntities = mutableListOf<Entity>()
	private val dynamicEntities = mutableListOf<Entity>()
	private val playerEntities = mutableListOf<Entity>()

	fun createEntity(func: EntityCreator.() -> Unit): Entity {
		val entity = EntityCreator.create(func)

		if(EntityManager.hasComponent(entity, PlayerComponent::class)) {
			addPlayerEntity(entity)
		} else {
			val physicsComponent = entity.getComponent(PhysicsEntityComponent::class)
			when (physicsComponent.body.motionType) {
				BodyMotionType.Static -> addStaticEntity(entity)
				BodyMotionType.Kinematic -> addStaticEntity(entity)
				BodyMotionType.Dynamic -> addDynamicEntity(entity)
			}
		}

		return entity
	}


	private fun addStaticEntity(entity: Entity) {
		staticEntities.add(entity)
	}
	private fun addDynamicEntity(entity: Entity) {
		dynamicEntities.add(entity)
	}

	private fun addPlayerEntity(entity: Entity) {
		playerEntities.add(entity)
	}

	fun toJson() = toJson(this)

	companion object {
		fun toJson(level: Level) = JSON.stringify(level)
		fun fromJson(json: String) = JSON.parse<Level>(json)
	}
}