package game.levels

import ecs.components.EnergyComponent
import ecs.components.PlayerComponent
import ecs.components.health.HealthComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointMemoryComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.graphics.ui.UserInterface
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import game.checkpoints.CheckpointManager
import tests.Assert
import general.Double2
import engine.types.Rgba
import kotlinx.serialization.Serializable

abstract class Level(val id: String) {
	private val staticEntities = mutableListOf<Entity>()
	private val dynamicEntities = mutableListOf<Entity>()
	private val playerEntities = mutableListOf<Entity>()
	private val checkpointEntities = mutableListOf<Entity>()
	private val bodylessEntities = mutableListOf<Entity>()
	private val loadedEntities = mutableListOf<Entity>()

	protected val checkpointManager = CheckpointManager()

	abstract val isGameLevel: Boolean

	fun load() {
		if (isGameLevel) {
			UserInterface.showUI()
		} else {
			UserInterface.hideUI()
		}

		loadLevel()
	}

	protected abstract fun loadLevel()

	fun unload() {
		staticEntities.forEach(this::removeEntity)
		dynamicEntities.forEach(this::removeEntity)
		playerEntities.forEach(this::removeEntity)
		checkpointEntities.forEach(this::removeEntity)
		bodylessEntities.forEach(this::removeEntity)
		loadedEntities.forEach(this::removeEntity)

		unloadLevel()
	}

	protected open fun unloadLevel() = Unit

	private fun removeEntity(entity: Entity) {
		EntityManager.removeEntitySafe(entity)
	}

	protected fun load(json: String) {
		loadedEntities.addAll(EntityManager.deserialize(json))
	}

	protected fun generatePlayerBodyBuilder() = MutableBodyBuilder(
		Circle(3.0),
		BodyMotionType.Dynamic
	).apply {
		fillColor = Rgba.WHITE
		transform.position = Double2(70.0, 50.0)
		friction = 0.1
	}


	protected fun initializePlayer(
		startAtCheckpoint: CheckpointComponent = checkpointEntities[0].getComponent(),
		checkpointCount: Int = checkpointEntities.size
	): Entity {
		val playerBodyBuilder = generatePlayerBodyBuilder().apply {
			this.transform.position = startAtCheckpoint.respawnPosition
		}

		return createEntityWithBody {
			bodyBuilder = playerBodyBuilder
			isPlayer = true
			canReceiveModifiers = true
			follow = true
			addComponent { EnergyComponent(100.0, 70.0, 150.0) }
			addComponent { CheckpointMemoryComponent(startAtCheckpoint, checkpointCount) }
			addComponent { HealthComponent(100.0) }
		}
	}

	protected fun createEntityWithBody(func: EntityCreator.() -> Unit): Entity {
		val entity = EntityCreator.createWithBody(func)

		when {
			EntityManager.hasComponent(entity, PlayerComponent::class) -> addPlayerEntity(entity)
			EntityManager.hasComponent(entity, PhysicsEntityComponent::class) -> {
				val physicsComponent = entity.getComponent<PhysicsEntityComponent>()
				when (physicsComponent.body.motionType) {
					BodyMotionType.Static -> addStaticEntity(entity)
					BodyMotionType.Kinematic -> addStaticEntity(entity)
					BodyMotionType.Dynamic -> addDynamicEntity(entity)
				}
			}
			else -> addStaticEntity(entity)
		}

		return entity
	}

	protected fun createEntityWithoutBody(func: EntityCreator.() -> Unit): Entity {
		val entity = EntityCreator.createWithoutBody(func)
		bodylessEntities.add(entity)
		return entity
	}

	fun addCheckpoint(func: EntityCreator.() -> Unit): Entity {
		val entity = EntityCreator.createWithBody(Graphics.staticForegroundContainer, func)
		Assert.isTrue(
			EntityManager.hasComponent(entity, CheckpointComponent::class),
			"Checkpoint must have checkpoint component"
		)
		checkpointEntities.add(entity)
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

	override fun toString(): String {
		return "Level(id: $id)"
	}

	companion object {
		fun toJson(level: Level) = JSON.stringify(level)
		fun fromJson(json: String) = JSON.parse<Level>(json)
	}
}