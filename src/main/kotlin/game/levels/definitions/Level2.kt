package game.levels.definitions

import ecs.components.LifeTimeComponent
import ecs.components.SpawnerComponent
import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
import ecs.components.health.InstantDestructionComponent
import ecs.components.triggers.CheckpointType
import ecs.eventsystem.CheckpointEventSystem
import ecs.eventsystem.DamageEventSystem
import ecs.eventsystem.DestroyDamageEventSystem
import ecs.eventsystem.ModifierEventSystem
import engine.graphics.Graphics
import engine.physics.bodies.shapes.Circle
import engine.physics.Physics
import engine.physics.bodies.shapes.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.system.EventSystemManager
import engine.types.Rgba
import game.levels.CollectionLevel
import game.levels.EntityCreator
import game.levels.Level
import general.Double2

class Level2 : CollectionLevel("Level2") {
	override val nextLevelId: String? = "Level3"

	override fun loadLevel() {
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
			CheckpointEventSystem(eventManager),
			DestroyDamageEventSystem(eventManager)
		)

	}

	private fun initializeCheckpoints() {
		val startAt = Double2(55, 15)
		val startCheckpoint = checkpointManager.createCheckpoint(startAt, CheckpointType.Start)

		val checkpointColor = Rgba.BLUE.apply { alpha = 100U }

		addCheckpoint {
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = checkpointColor
				transform.position = startAt
				isSensor = true
			}
			addComponent { startCheckpoint }
		}

		addCheckpoint {
			val position = Double2(120, 185)
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = checkpointColor
				this.transform.position = position
				isSensor = true
			}
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.Standard) }
		}

		addCheckpoint {
			val position = Double2(180, 94)
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = checkpointColor
				this.transform.position = position
				isSensor = true
			}
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.Standard) }
		}

		addCheckpoint {
			val position = Double2(340, 185)
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = checkpointColor
				this.transform.position = position
				isSensor = true
			}
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.Standard) }
		}

		addCheckpoint {
			val position = Double2(550, 42.5)
			bodyBuilder = MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = checkpointColor
				this.transform.position = position
				isSensor = true
			}
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.End) }
		}
	}

	private fun buildStatics() {
		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(10, 2), BodyMotionType.Static).apply {
				transform.position = Double2(55, 21)
				fillColor = Rgba.FOREST_GREEN
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(20, 2), BodyMotionType.Static).apply {
				transform.position = Double2(180, 100)
				fillColor = Rgba.GRAY
			}
		}

	}

	private fun initializeSpecials() {
		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Circle(7), BodyMotionType.Static).apply {
				fillColor = Rgba.RED
				transform.position = Double2(40, 40)
			}
			addComponent { DamageComponent(400.0) }
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Circle(7), BodyMotionType.Static).apply {
				fillColor = Rgba.RED
				transform.position = Double2(20, 60)
			}
			addComponent { DamageComponent(400.0) }
		}

		createEntityWithoutBody {
			val sphereBodyBuilder = MutableBodyBuilder(Circle(5), BodyMotionType.Dynamic).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(150, 70)
				restitution = 0.3
			}
			addComponent {
				SpawnerComponent(sphereBodyBuilder, 1.3, {
					it.density = 0.01 + kotlin.random.Random.nextDouble() * 100.0
					it.friction = 0.01 + kotlin.random.Random.nextDouble()
					addComponent { LifeTimeComponent(2.3) }
					addComponent { HealthComponent(Double.POSITIVE_INFINITY) }
				})
			}
		}

		createEntityWithoutBody {
			val sphereBodyBuilder = MutableBodyBuilder(Circle(5), BodyMotionType.Dynamic).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(215, 70)
				restitution = 0.3
			}
			addComponent {
				SpawnerComponent(sphereBodyBuilder, 1.7, {
					it.density = 0.01 + kotlin.random.Random.nextDouble() * 100.0
					it.friction = 0.01 + kotlin.random.Random.nextDouble()
					addComponent { LifeTimeComponent(2.3) }
					addComponent { HealthComponent(Double.POSITIVE_INFINITY) }
				})
			}
		}

		EntityCreator().apply {
			bodyBuilder = MutableBodyBuilder(Rectangle(30, 20), BodyMotionType.Static).apply {
				transform.position = Double2(180, 200)
				fillColor = Rgba.RED
			}
			usePhysics = false
		}.createWithBody(Graphics.staticForegroundContainer)


		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(30, 10), BodyMotionType.Static).apply {
				transform.position = Double2(180, 200)
				fillColor = Rgba.NONE
				isSensor = true
			}

			addComponent { InstantDestructionComponent() }
		}
	}

	private fun loadBounds() {

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 190), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(-5, 95)
						restitution = 0.7
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(30, 0)
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 160), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(75, 75)
						restitution = 0.4
					}

		}


		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(175, 10), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(77.5, 195)
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(175, 10), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(282.5, 195)
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(120, 150)
						restitution = 0.4
					}

		}

		//funnel
		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(160, 150)
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(147.35, 119.6)
						transform.angleDegrees = -45.0
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 50), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(132.5, 85)
						restitution = 0.4
					}

		}

		//funnel inverse
		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(200, 150)
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(212.5, 119.6)
						transform.angleDegrees = 45.0
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(10, 50), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(227.5, 85)
						restitution = 0.4
					}

		}

		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(100, 20), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(180, 60)
						restitution = 0.4
					}
		}

		//right part of the map


		createEntityWithBody {
			bodyBuilder =
					MutableBodyBuilder(Rectangle(175, 10), BodyMotionType.Static).apply {
						fillColor = Rgba.GRAY
						transform.position = Double2(282.5, 150)
						restitution = 0.4
					}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(200, 10), BodyMotionType.Static).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(430, 75)
				transform.angleDegrees = -50.0
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(200, 10), BodyMotionType.Static).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(430, 125)
				transform.angleDegrees = -50.0
				restitution = 0.0
				friction = 0.0
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Static).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(530, 52.5)
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Static).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(530, 0)
			}
		}

		createEntityWithBody {
			bodyBuilder = MutableBodyBuilder(Rectangle(10, 60), BodyMotionType.Static).apply {
				fillColor = Rgba.GRAY
				transform.position = Double2(570, 25)
			}
		}

	}

}