package game.levels.definitions

import ecs.components.DamageComponent
import ecs.components.RotateMeComponent
import ecs.components.SpawnerComponent
import ecs.components.triggers.CheckpointType
import ecs.eventsystem.CheckpointEventSystem
import ecs.eventsystem.DamageEventSystem
import ecs.eventsystem.ModifierEventSystem
import engine.physics.Circle
import engine.physics.Physics
import engine.physics.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.system.EventSystemManager
import game.levels.Level
import utility.Double2
import utility.Rgba

class Level2 : Level("level2") {
	override fun load() {
		loadBounds()
		buildStatics()
		initializeSpecials()
		initializeCheckpoints()
		initializeEventSystems()
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
		val startAt = Double2(55, 15)
		val startCheckpoint = checkpointManager.createCheckpoint(startAt, CheckpointType.Start)

		addCheckpoint {
			setBodyBuilder(MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = Rgba.BLUE
				position = startAt
				isSensor = true
			})
			addComponent { startCheckpoint }
		}

		addCheckpoint {
			val position = Double2(-55, -25)
			setBodyBuilder(MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = Rgba.BLUE
				this.position = position
				isSensor = true
			})
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.Standard) }
		}

		addCheckpoint {
			val position = Double2(95, 45)
			setBodyBuilder(MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = Rgba.BLUE
				this.position = position
				isSensor = true
			})
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.Standard) }
		}

		addCheckpoint {
			val position = Double2(95, -25)
			setBodyBuilder(MutableBodyBuilder(Rectangle(10.0, 10.0), BodyMotionType.Static).apply {
				fillColor = Rgba.BLUE
				this.position = position
				isSensor = true
			})
			addComponent { checkpointManager.createCheckpoint(position, CheckpointType.End) }
		}

		initializePlayer()
	}

	private fun buildStatics() {
		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(48.0, 4.0), BodyMotionType.Kinematic).apply {
					position = Double2(0, 10)
					restitution = 1.0
					fillColor = Rgba.YELLOW
					friction = 0.0
				}
			)
			addComponent { RotateMeComponent(45.0) }
		}


		createEntityWithBody {
			setBodyBuilder(MutableBodyBuilder(Rectangle(10, 2), BodyMotionType.Static).apply {
				position = Double2(55, 21)
				fillColor = Rgba.FOREST_GREEN
			})
		}

	}

	private fun initializeSpecials() {
		/*createEntityWithBody {
			setBodyBuilder(MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Kinematic).apply {
				fillColor = Rgba.RED
				position = Double2(10, 45)
				isSensor = true
			})
			addComponent { DamageComponent(200.0) }
		}*/

		createEntityWithBody {
			setBodyBuilder(MutableBodyBuilder(Circle(7), BodyMotionType.Static).apply {
				fillColor = Rgba.RED
				position = Double2(40, 40)
			})
			addComponent { DamageComponent(400.0) }
		}

		createEntityWithBody {
			setBodyBuilder(MutableBodyBuilder(Circle(7), BodyMotionType.Static).apply {
				fillColor = Rgba.RED
				position = Double2(20, 60)
			})
			addComponent { DamageComponent(400.0) }
		}

		createEntityWithoutBody {
			val sphereBodyBuilder = MutableBodyBuilder(Circle(5), BodyMotionType.Dynamic).apply {
				fillColor = Rgba.GRAY
				position = Double2(150, 70)
			}
			addComponent { SpawnerComponent(sphereBodyBuilder, 2.0) }
		}

		createEntityWithoutBody {
			val sphereBodyBuilder = MutableBodyBuilder(Circle(5), BodyMotionType.Dynamic).apply {
				fillColor = Rgba.GRAY
				position = Double2(215, 70)
				density = 0.1
			}
			addComponent { SpawnerComponent(sphereBodyBuilder, 2.0) }
		}
	}

	private fun loadBounds() {

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10, 190), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(-5, 95)
					restitution = 0.7
				}
			)
		}

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(30, 0)
					restitution = 0.4
				}
			)
		}

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10, 160), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(75, 75)
					restitution = 0.4
				}
			)
		}


		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(175, 10), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(77.5, 195)
					restitution = 0.4
				}
			)
		}

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(175, 10), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(282.5, 195)
					restitution = 0.4
				}
			)
		}

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(120, 150)
					restitution = 0.4
				}
			)
		}

		//funnel
		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(160, 150)
					restitution = 0.4
				}
			)
		}

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(147.35, 119.6)
					restitution = 0.4
					angle = -45.0
				}
			)
		}

		//funnel inverse
		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(200, 150)
					restitution = 0.4
				}
			)
		}

		createEntityWithBody {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10, 40), BodyMotionType.Static).apply {
					fillColor = Rgba.GRAY
					position = Double2(212.5, 119.6)
					restitution = 0.4
					angle = 45.0
				}
			)
		}

	}

}