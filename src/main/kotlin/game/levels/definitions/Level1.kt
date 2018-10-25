package game.levels.definitions

import ecs.components.RotateMeComponent
import engine.Graphics
import engine.physics.BodyBuilder
import engine.physics.Circle
import engine.physics.Rectangle
import game.levels.EntityCreator
import game.modifiers.ShapeModifierFactory
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

	private fun generatePlayerBodyBuilder() = BodyBuilder()
		.setShape(Circle(10.0))
		.setFillColor(Rgba.BLUE)
		.setPosition(70.0, 50.0)
		.setLineWidth(3.0)
		.setFriction(0.1)
		.setFrictionAir(0.0)
		.setFrictionStatic(0.3)

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
		val builder = BodyBuilder()
			.setFillColor(Rgba.GRAY)
			.setRestitution(0.5)
			.setFriction(0.01)
			.setFrictionAir(0.0)

		val smallerSize = kotlin.math.min(width, height)

		val length = smallerSize.toDouble() / 2.0

		EntityCreator.create(Graphics.staticForegroundContainer) {
			setBodyBuilder(
				BodyBuilder()
					.setShape(Rectangle(length, 20.0))
					.setPosition(halfWidth, halfHeight)
					.setStatic(true)
					.setRestitution(0.1)
					.setFillColor(Rgba.YELLOW)
			)
			addComponent { RotateMeComponent(1.0) }
		}

		EntityCreator.create(Graphics.staticForegroundContainer) {
			setBodyBuilder(
				builder
					.setPosition(halfWidth, halfHeight - length / 4.0)
					.setShape(Rectangle(halfWidth, 10.0))
					.setStatic(true)
			)
		}

		EntityCreator.create(Graphics.staticForegroundContainer) {
			setBodyBuilder(
				builder
					.setPosition(halfWidth, halfHeight + length / 4.0)
					.setShape(Rectangle(halfWidth, 10.0))
					.setStatic(true)
			)
			addModifier(ShapeModifierFactory().apply {
				setBodyBuilder(
					generatePlayerBodyBuilder()
						.setShape(Rectangle(length / 3, length / 4))
						.setRestitution(0.0))
				setTimeLeft(5.0)
			})
		}
	}

	private fun loadBounds() {
		val color = Rgba(145U, 0U, 0U)

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder()
					.setShape(Rectangle(width.toDouble(), 200.0))
					.setFillColor(color)
					.setPosition(halfWidth, height.toDouble() + 80.0)
					.setStatic(true)
					.setRestitution(0.4)
			)
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder()
					.setShape(Rectangle(width.toDouble(), 200.0))
					.setFillColor(color)
					.setPosition(halfWidth, -80.0)
					.setStatic(true)
					.setRestitution(0.4)
			)
		}

		val squareBody = generatePlayerBodyBuilder().setShape(Rectangle(40.0, 40.0))

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder()
					.setShape(Rectangle(200.0, height.toDouble()))
					.setFillColor(color)
					.setPosition(-80.0, halfHeight)
					.setStatic(true)
					.setRestitution(0.4)
			)
			addModifier(ShapeModifierFactory().apply {
				setBodyBuilder(squareBody)
				setTimeLeft(5.0)
			})
		}

		EntityCreator.create(Graphics.staticBackgroundContainer) {
			setBodyBuilder(
				BodyBuilder()
					.setShape(Rectangle(200.0, height.toDouble()))
					.setFillColor(color)
					.setPosition(width + 80.0, halfHeight)
					.setStatic(true)
					.setRestitution(0.4)
			)
		}
	}
}