package engine.physics

import Matter.Body
import jslib.pixi.Graphics
import BodyRender
import utility.Double2
import utility.Rgba


class BodyBuilder {
	private var isStatic = false
	private var shape: IShape? = null
	private var fillColor: Rgba = Rgba.BLACK
	private var outlineColor: Rgba = Rgba.BLACK
	private var position: Double2 = Double2()

	private var restitution: Double = 0.0
	private var friction: Double = 0.1
	private var frictionStatic: Double = 0.5
	private var frictionAir: Double = 0.01
	private var density: Double? = null

	private var lineWidth: Double = 0.0

	fun setShape(shape: IShape): BodyBuilder {
		this.shape = shape
		return this
	}

	fun setStatic(isStatic: Boolean): BodyBuilder {
		this.isStatic = isStatic
		return this
	}

	fun setFillColor(color: Rgba): BodyBuilder {
		this.fillColor = color
		return this
	}

	fun setOutlineColor(color: Rgba): BodyBuilder {
		this.outlineColor = color
		return this
	}

	fun setLineWidth(value: Double): BodyBuilder {
		this.lineWidth = value
		return this
	}

	fun setPosition(position: Double2): BodyBuilder {
		this.position = position
		return this
	}

	fun setPosition(x: Double, y: Double): BodyBuilder {
		return setPosition(Double2(x, y))
	}

	fun setElasticity(value: Double): BodyBuilder {
		restitution = value
		return this
	}

	fun setFriction(value: Double): BodyBuilder {
		friction = value
		return this
	}

	fun setFrictionAir(value: Double): BodyBuilder {
		frictionAir = value
		return this
	}

	fun setFrictionStatic(value: Double): BodyBuilder {
		frictionStatic = value
		return this
	}

	fun setDensity(value: Double): BodyBuilder {
		density = value
		return this
	}

	private fun buildBody(): Body {
		val body = shape!!.buildBody(position)
		val render = BodyRender()

		render.fillStyle = fillColor.rgbaString
		render.strokeStyle = outlineColor.rgbaString
		render.lineWidth = lineWidth

		if (body.isStatic != isStatic)
			Body.setStatic(body, isStatic)

		if (density != null)
			Body.setDensity(body, density!!)

		body.friction = friction
		body.frictionAir = frictionAir
		body.frictionStatic = frictionStatic

		body.restitution = restitution

		body.render = render

		return body
	}

	private fun buildGraphics(): Graphics {
		return Graphics().apply {
			lineStyle(lineWidth, 0xFFFFFFFF)
			this.beginFill(fillColor.rgb)

			val shape = shape!!
			when (shape) {
				is Circle -> drawCircle(position.x, position.y, shape.radius)
				is Rectangle -> {
					drawRect(0, 0, shape.width, shape.height)
					pivot.x = shape.width / 2.0
					pivot.y = shape.height / 2.0
				}
				else -> throw NotImplementedError("Shape ${shape::class.simpleName} is not yet supported")
			}

			endFill()
		}
	}

	fun build(): Pair<Body, Graphics> = Pair(buildBody(), buildGraphics())
}
