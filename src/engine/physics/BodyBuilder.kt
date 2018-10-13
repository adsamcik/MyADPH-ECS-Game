package engine.physics

import Matter.Body
import Render
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

	fun setLineWidth(value: Double) : BodyBuilder {
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

	fun setFriction(value: Double) : BodyBuilder {
		friction = value
		return this
	}

	fun setFrictionAir(value: Double) : BodyBuilder {
		frictionAir = value
		return this
	}

	fun setFrictionStatic(value: Double) : BodyBuilder {
		frictionStatic = value
		return this
	}

	fun build(): Body {
		val body = shape!!.buildBody(position)
		val render = Render()

		render.fillStyle = fillColor.rgbaString
		render.strokeStyle = outlineColor.rgbaString
		render.lineWidth = lineWidth

		if (body.isStatic != isStatic)
			Body.setStatic(body, isStatic)

		body.friction = friction
		body.frictionAir = frictionAir
		body.frictionStatic = frictionStatic

		body.restitution = restitution

		body.render = render

		return body
	}
}
