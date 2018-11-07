package game.levels.definitions

import ecs.components.RotateMeComponent
import engine.Graphics
import engine.physics.Circle
import engine.physics.Rectangle
import engine.physics.bodies.BodyBuilder
import engine.physics.bodies.BodyMotionType
import game.levels.EntityCreator
import game.modifiers.ShapeModifierFactory
import utility.Double2
import utility.Rgba
import kotlin.browser.window

class Level1 {
	val width = window.innerWidth
	val height = window.innerHeight

	val halfWidth = window.innerWidth / 2.0
	val halfHeight = window.innerHeight / 2.0

	fun load() {
		loadBounds()
		buildStatics()
		initializePlayer()
	}

	private fun generatePlayerBodyBuilder() = BodyBuilder(Circle(3.0)).apply {
		fillColor = Rgba.WHITE
		position = Double2(70.0, 50.0)
		friction = 0.1
		motionType = BodyMotionType.Dynamic
	}

	private fun initializePlayer() {
		val playerBodyBuilder = generatePlayerBodyBuilder()

		EntityCreator.create {
			setBodyBuilder(playerBodyBuilder)
			setPlayer(true)
			setReceiveModifiers(true)
			setFollow(true)
		}
	}

	private fun buildStatics() {
		val builder = BodyBuilder(Rectangle(0.0, 0.0)).apply {
			fillColor = Rgba.GRAY
			restitution = 0.5
			friction = 0.01
		}

		val length = 50.0

		EntityCreator.create {
			setBodyBuilder(
				BodyBuilder(Rectangle(length, 4.0)).apply {
					position = Double2(0, 0)
					motionType = BodyMotionType.Kinematic
					restitution = 0.1
					fillColor = Rgba.YELLOW
					friction = 1.0
				}
			)
			addComponent { RotateMeComponent(1.0) }
		}

		EntityCreator.create {
			setBodyBuilder(
				builder.apply {
					position = Double2(0.0, -length / 4.0)
					shape = Rectangle(10.0, 2.0)
					motionType = BodyMotionType.Static
				}
			)
		}

		EntityCreator.create {
			setBodyBuilder(
				builder.apply {
					position = Double2(0.0, length / 4.0)
					shape = Rectangle(10.0, 2.0)
					motionType = BodyMotionType.Static
				}
			)
		}
	}

	private fun loadBounds() {
		val color = Rgba(145U, 0U, 0U)
		val squareBody = generatePlayerBodyBuilder().apply {
			shape = Rectangle(5.0, 5.0)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder(Rectangle(200.0, 20.0)).apply {
					fillColor = color
					position = Double2(0, -100)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder(Rectangle(20.0, 200.0)).apply {
					fillColor = color
					position = Double2(-100, 0)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder(Rectangle(200.0, 20.0)).apply {
					fillColor = color
					position = Double2(0, 100)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder(Rectangle(20.0, 200.0)).apply {
					fillColor = color
					position = Double2(100, 0)
					motionType = BodyMotionType.Static
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