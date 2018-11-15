package game.levels.definitions

import ecs.components.DamageComponent
import ecs.components.RotateMeComponent
import ecs.components.triggers.CheckpointType
import ecs.eventsystem.CheckpointEventSystem
import ecs.eventsystem.DamageEventSystem
import ecs.eventsystem.ModifierEventSystem
import engine.physics.Physics
import engine.physics.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.system.EventSystemManager
import game.levels.Level
import utility.Double2
import utility.Rgba

class Level1 : Level("level1") {
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
		val startAt = Double2(-90, 45)
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
		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(48.0, 4.0), BodyMotionType.Kinematic).apply {
					position = Double2(0, 10)
					restitution = 1.0
					fillColor = Rgba.YELLOW
					friction = 0.0
				}
			)
			addComponent { RotateMeComponent(90.0) }
		}

		for (i in 0..4) {
			createEntity {
				setBodyBuilder(MutableBodyBuilder(Rectangle(20, 20), BodyMotionType.Static).apply {
					position = Double2(-80 + i * 30, -i * 10)
					fillColor = Rgba.SKY_BLUE
					friction = 0.3
					restitution = 0.0
				})
			}
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(50, 30), BodyMotionType.Static).apply {
				position = Double2(-55, 35)
				fillColor = Rgba.FOREST_GREEN
			})
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(40, 40), BodyMotionType.Static).apply {
				position = Double2(70, 30)
				fillColor = Rgba.FOREST_GREEN
			})
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(90, 5), BodyMotionType.Static).apply {
				position = Double2(55, -17.5)
				fillColor = Rgba.SKY_BLUE
				friction = 0.0
				restitution = 0.1
			})
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(10, 10), BodyMotionType.Static).apply {
				position = Double2(65, -25)
				fillColor = Rgba.SKY_BLUE
			})
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(10, 10), BodyMotionType.Static).apply {
				position = Double2(65, -45)
				fillColor = Rgba.SKY_BLUE
			})
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(10, 10), BodyMotionType.Static).apply {
				position = Double2(85, -25)
				fillColor = Rgba.SKY_BLUE
			})
		}

		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(10, 10), BodyMotionType.Static).apply {
				position = Double2(85, -35)
				fillColor = Rgba.SKY_BLUE
			})
		}

	}

	private fun initializeSpecials() {
		createEntity {
			setBodyBuilder(MutableBodyBuilder(Rectangle(80, 10), BodyMotionType.Kinematic).apply {
				fillColor = Rgba.RED
				position = Double2(10, 45)
				isSensor = true
			})
			addComponent { DamageComponent(200.0) }
		}
	}

	private fun loadBounds() {
		val boundsColor = Rgba.RED.apply {
			alpha = 25U
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(220.0, 10.0), BodyMotionType.Static).apply {
					fillColor = boundsColor
					position = Double2(0, -55)
					restitution = 0.4
				}
			)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10.0, 120.0), BodyMotionType.Static).apply {
					fillColor = boundsColor
					position = Double2(-105, 0)
					restitution = 0.4
				}
			)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(220.0, 10.0), BodyMotionType.Static).apply {
					fillColor = boundsColor
					position = Double2(0, 55)
					restitution = 0.4
				}
			)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(10.0, 120.0), BodyMotionType.Static).apply {
					fillColor = boundsColor
					position = Double2(105, 0)
					restitution = 0.4
				}
			)
		}
	}
}