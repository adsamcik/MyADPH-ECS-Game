package game.levels.definitions

import ecs.components.RotateMeComponent
import ecs.components.health.DamageComponent
import ecs.components.triggers.CheckpointType
import ecs.eventsystem.CheckpointEventSystem
import ecs.eventsystem.DamageEventSystem
import ecs.eventsystem.ModifierEventSystem
import engine.physics.bodies.shapes.Circle
import engine.physics.Physics
import engine.physics.bodies.shapes.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.system.EventSystemManager
import engine.types.Rgba
import game.levels.Level
import game.modifiers.ShapeModifierFactory
import general.Double2

class Level3 : Level("Level3") {
	override fun load() {
		loadBounds()
		buildStatics()
		initializeSpecials()
		initializeCheckpoints()
		initializeEventSystems()
		initializePlayer()
	}

	private fun initializeEventSystems() {
		val eventManager = Physics.engine.eventManager
		EventSystemManager.tryRegisterSystems(
			ModifierEventSystem(eventManager),
			DamageEventSystem(eventManager),
			CheckpointEventSystem(eventManager)
		)
	}

	private fun initializeCheckpoints() {
		val startAt = Double2(-90, 45)
		val startCheckpoint = checkpointManager.createCheckpoint(startAt, CheckpointType.Start)

		addCheckpoint {
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = Rgba.BLUE
				this.transform.position = startAt
				isSensor = true
			}
			addComponent { startCheckpoint }
		}


		addCheckpoint {
			val position = Double2(95, 45)
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = Rgba.BLUE
				this.transform.position = position
				isSensor = true
			}
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.End) }
		}
	}

	private fun buildStatics() {
		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(48.0, 4.0), BodyMotionType.Kinematic).apply {
						transform.position = Double2(0, 10)
						restitution = 1.0
						fillColor = Rgba.YELLOW
						friction = 0.0
					}

			addComponent { RotateMeComponent(90.0) }
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(50, 30), BodyMotionType.Static).apply {
				transform.position = Double2(-55, 35)
				fillColor = Rgba.FOREST_GREEN
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(40, 40), BodyMotionType.Static).apply {
				transform.position = Double2(70, 30)
				fillColor = Rgba.FOREST_GREEN
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(40, 40), BodyMotionType.Static).apply {
				transform.position = Double2(80, -15)
				fillColor = Rgba.FOREST_GREEN
			}
		}


	}

	private fun initializeSpecials() {
		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Kinematic).apply {
				fillColor = Rgba.RED
				transform.position = Double2(10, 45)
				isSensor = true
			}
			addComponent { DamageComponent(200.0) }
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Circle(2), BodyMotionType.Static).apply {
				fillColor = Rgba.ORANGE
				transform.position = Double2(0,20)
				isSensor = true
			}
			addModifier(ShapeModifierFactory().apply {
				bodyBuilder = generatePlayerBodyBuilder().apply {
					shape = Circle(2)
				}
				timeLeft = 3.0
			})
		}
	}

	private fun loadBounds() {
		val boundsColor = Rgba.RED.apply {
			alpha = 25U
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(220.0, 10.0), BodyMotionType.Static).apply {
				fillColor = boundsColor
				transform.position = Double2(0, -55)
				restitution = 0.4
			}

		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 120.0), BodyMotionType.Static).apply {
				fillColor = boundsColor
				transform.position = Double2(-105, 0)
				restitution = 0.4
			}

		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(220.0, 10.0), BodyMotionType.Static).apply {
				fillColor = boundsColor
				transform.position = Double2(0, 55)
				restitution = 0.4
			}

		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 120.0), BodyMotionType.Static).apply {
				fillColor = boundsColor
				transform.position = Double2(105, 0)
				restitution = 0.4
			}

		}
	}
}