package game.levels.definitions

import ecs.components.RotateMeComponent
import engine.Graphics
import engine.physics.BodyBuilder
import engine.physics.Circle
import engine.physics.Rectangle
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

	private fun generatePlayerBodyBuilder() = BodyBuilder().apply {
		shape = Circle(10.0)
		fillColor = Rgba.BLUE
		position = Double2(70.0, 50.0)
		lineWidth = 3.0
		friction = 0.1
	}

	private fun initializePlayer() {
		val playerBodyBuilder = generatePlayerBodyBuilder()

		EntityCreator.create(Graphics.dynamicContainer) {
			setBodyBuilder(playerBodyBuilder)
			setPlayer(true)
			setReceiveModifiers(true)
			setFollow(true)
		}
	}

	private fun buildStatics() {
		val builder = BodyBuilder().apply {
			fillColor = Rgba.GRAY
			restitution = 0.5
			friction = 0.01
		}

		val smallerSize = kotlin.math.min(width, height)

		val length = smallerSize.toDouble() / 2.0

		EntityCreator.create(Graphics.staticForegroundContainer) {
			setBodyBuilder(
				BodyBuilder().apply {
					shape = Rectangle(length, 20.0)
					position = Double2(halfWidth, halfHeight)
					motionType = BodyMotionType.Static
					restitution = 0.1
					fillColor = Rgba.YELLOW
				}
			)
			addComponent { RotateMeComponent(1.0) }
		}

		EntityCreator.create(Graphics.staticForegroundContainer) {
			setBodyBuilder(
				builder.apply {
					position = Double2(halfWidth, halfHeight - length / 4.0)
					shape = Rectangle(halfWidth, 10.0)
					motionType = BodyMotionType.Static
				}
			)
		}

		EntityCreator.create(Graphics.staticForegroundContainer) {
			setBodyBuilder(
				builder.apply {
					position = Double2(halfWidth, halfHeight + length / 4.0)
					shape = Rectangle(halfWidth, 10.0)
					motionType = BodyMotionType.Static
				}
			)
			addModifier(ShapeModifierFactory().apply {
				setBodyBuilder(
					generatePlayerBodyBuilder().apply {
						shape = Rectangle(length / 3.0, length / 4.0)
						restitution = 0.0
					})
				setTimeLeft(5.0)
			})
		}
	}

	private fun loadBounds() {
		val color = Rgba(145U, 0U, 0U)

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder().apply {
					shape = Rectangle(width.toDouble(), 200.0)
					fillColor = color
					position = Double2(halfWidth, height.toDouble() + 80)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder().apply {
					shape = Rectangle(width.toDouble(), 200.0)
					fillColor = color
					position = Double2(halfWidth, -80.0)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
		}

		val squareBody = generatePlayerBodyBuilder().apply {
			shape = Rectangle(40.0, 40.0)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder().apply {
					shape = Rectangle(200.0, height.toDouble())
					fillColor = color
					position = Double2(-80.0, halfHeight)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
			addModifier(ShapeModifierFactory().apply {
				setBodyBuilder(squareBody)
				setTimeLeft(5.0)
			})
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder().apply {
					shape = Rectangle(200.0, height.toDouble())
					fillColor = color
					position = Double2(width + 80.0, halfHeight)
					motionType = BodyMotionType.Static
					restitution = 0.4
				}
			)
		}
	}
}