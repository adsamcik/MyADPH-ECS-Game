package game.levels

import ecs.components.EnergyComponent
import ecs.components.health.HealthComponent
import ecs.components.PlayerComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointMemoryComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.Circle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import game.checkpoints.CheckpointManager
import utility.Assert
import utility.Double2
import utility.Rgba

abstract class Level(val id: String) {
	private val staticEntities = mutableListOf<Entity>()
	private val dynamicEntities = mutableListOf<Entity>()
	private val playerEntities = mutableListOf<Entity>()
	private val checkpointEntities = mutableListOf<Entity>()
	private val bodylessEntities = mutableListOf<Entity>()

	protected val checkpointManager = CheckpointManager()


	abstract fun load()

	fun unload() {
		staticEntities.forEach(this::removeEntity)
		dynamicEntities.forEach(this::removeEntity)
		playerEntities.forEach(this::removeEntity)
		checkpointEntities.forEach(this::removeEntity)
		bodylessEntities.forEach(this::removeEntity)
	}

	private fun removeEntity(entity: Entity) {
		EntityManager.removeEntitySafe(entity)
	}

	protected fun generatePlayerBodyBuilder() = MutableBodyBuilder(
		Circle(3.0),
		BodyMotionType.Dynamic
	).apply {
		fillColor = Rgba.WHITE
		position = Double2(70.0, 50.0)
		friction = 0.1
	}


	protected fun initializePlayer(
		startAtCheckpoint: CheckpointComponent = checkpointEntities[0].getComponent(),
		checkpointCount: Int = checkpointEntities.size
	) {
		val playerBodyBuilder = generatePlayerBodyBuilder().apply {
			this.position = startAtCheckpoint.respawnPosition
		}

		createEntityWithBody {
			setBodyBuilder(playerBodyBuilder)
			setPlayer(true)
			setReceiveModifiers(true)
			setFollow(true)
			addComponent { EnergyComponent(100.0, 70.0, 150.0) }
			addComponent { CheckpointMemoryComponent(startAtCheckpoint, checkpointCount) }
			addComponent { HealthComponent(100.0) }
		}
	}

	protected fun createEntityWithBody(func: EntityCreator.() -> Unit): Entity {
		val entity = EntityCreator.createWithBody(func)

		if (EntityManager.hasComponent(entity, PlayerComponent::class)) {
			addPlayerEntity(entity)
		} else {
			val physicsComponent = entity.getComponent<PhysicsEntityComponent>()
			when (physicsComponent.body.motionType) {
				BodyMotionType.Static -> addStaticEntity(entity)
				BodyMotionType.Kinematic -> addStaticEntity(entity)
				BodyMotionType.Dynamic -> addDynamicEntity(entity)
			}
		}

		return entity
	}

	protected fun createEntityWithoutBody(func: EntityCreator.() -> Unit): Entity {
		val entity = EntityCreator.createWithoutBody(func)
		bodylessEntities.add(entity)
		return entity
	}

	fun addCheckpoint(func: EntityCreator.() -> Unit): Entity {
		val entity = createEntityWithBody(func)
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

	companion object {
		fun toJson(level: Level) = JSON.stringify(level)
		fun fromJson(json: String) = JSON.parse<Level>(json)
	}
}