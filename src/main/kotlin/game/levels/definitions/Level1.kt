package game.levels.definitions

import ecs.components.RotateMeComponent
import ecs.components.triggers.EndComponent
import ecs.components.triggers.StartComponent
import engine.physics.Circle
import engine.physics.Rectangle
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import game.levels.Level
import game.modifiers.ShapeModifierFactory
import utility.Double2
import utility.Rgba
import kotlin.browser.window

class Level1 : Level("level1") {
	val width = window.innerWidth
	val height = window.innerHeight

	val halfWidth = window.innerWidth / 2.0
	val halfHeight = window.innerHeight / 2.0

	fun load() {
		loadBounds()
		buildStatics()
		initializePlayer()
	}

	private fun generatePlayerBodyBuilder() = MutableBodyBuilder(
		Circle(3.0),
		BodyMotionType.Dynamic
	).apply {
		fillColor = Rgba.WHITE
		position = Double2(70.0, 50.0)
		friction = 0.1
	}

	private fun initializePlayer() {
		val playerBodyBuilder = generatePlayerBodyBuilder()

		createEntity {
			setBodyBuilder(playerBodyBuilder)
			setPlayer(true)
			setReceiveModifiers(true)
			setFollow(true)
		}
	}

	private fun buildStatics() {
		val builder = MutableBodyBuilder(Rectangle(10.0, 2.0), BodyMotionType.Static).apply {
			fillColor = Rgba.GRAY
			restitution = 0.5
			friction = 0.01
		}

		val length = 50.0

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(length, 4.0), BodyMotionType.Kinematic).apply {
					position = Double2(0, 0)
					restitution = 0.1
					fillColor = Rgba.YELLOW
					friction = 1.0
				}
			)
			addComponent { RotateMeComponent(1.0) }
		}

		createEntity {
			setBodyBuilder(
				builder.apply {
					position = Double2(0.0, -length / 4.0)
				}
			)
		}

		createEntity {
			setBodyBuilder(
				builder.apply {
					position = Double2(0.0, length / 4.0)
				}
			)
		}
	}

	private fun loadTriggers() {
		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(
					Rectangle(10.0, 10.0),
					BodyMotionType.Static
				).apply {
				fillColor = Rgba.GRAY
				isSensor = true
			})
			addComponent { StartComponent() }
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(
					Rectangle(10.0, 10.0),
					BodyMotionType.Static
				).apply {
				fillColor = Rgba.GREEN
				isSensor = true
			})
			addComponent { EndComponent() }
		}
	}

	private fun loadBounds() {
		val color = Rgba(145U, 0U, 0U)
		val squareBody = generatePlayerBodyBuilder().apply {
			shape = Rectangle(5.0, 5.0)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(200.0, 20.0), BodyMotionType.Static).apply {
					fillColor = color
					position = Double2(0, -100)
					restitution = 0.4
				}
			)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(20.0, 200.0), BodyMotionType.Static).apply {
					fillColor = color
					position = Double2(-100, 0)
					restitution = 0.4
				}
			)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(200.0, 20.0), BodyMotionType.Static).apply {
					fillColor = color
					position = Double2(0, 100)
					restitution = 0.4
				}
			)
		}

		createEntity {
			setBodyBuilder(
				MutableBodyBuilder(Rectangle(20.0, 200.0), BodyMotionType.Static).apply {
					fillColor = color
					position = Double2(100, 0)
					restitution = 0.4
				}
			)
			addModifier(ShapeModifierFactory().apply {
				setBodyBuilder(squareBody)
				setTimeLeft(5.0)
			})
		}
	}
}