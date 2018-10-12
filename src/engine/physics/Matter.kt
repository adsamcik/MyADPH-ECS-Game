import utility.Double2

abstract external class Matter {
	abstract class Engine {
		var enableSleeping: Boolean
		val world: World

		companion object {
			fun create(): Engine
			fun run(engine: Engine)
			fun update(engine: Engine, delta: Double = definedExternally, correction: Double = definedExternally)
		}
	}

	abstract class World : Composite {
		companion object {
			fun add(world: World, obj: dynamic): Composite
		}
	}

	abstract class Composite {
		val id: Int
		val bodies: Array<Body>
		val composites: Array<Composite>

		companion object {
			fun add(composite: Composite, obj: dynamic): Composite
			fun allBodies(composite: Composite): Array<Body>
		}
	}

	abstract class Bodies {
		companion object {
			fun circle(x: Double, y: Double, radius: Double, options: dynamic = definedExternally, maxSides: Double = definedExternally): Body
			fun fromVertices(x: Double, y: Double, vector: Array<Double2> = definedExternally, options: dynamic = definedExternally, flagInternal: Boolean = definedExternally, removeCollinear: Double = definedExternally, minimumArea: Double = definedExternally): Body
			fun polygon(x: Double, y: Double, sides: Int, radius: Double, options: dynamic = definedExternally): Body
			fun rectangle(x: Double, y: Double, width: Double, height: Double, options: dynamic = definedExternally): Body
		}
	}

	abstract class Body {
		val vertices: Array<Double2>
		val restitution: Double
		val sleepThreshold: Int
		val slop: Double
		val speed: Double
		val timeScale: Double
		val torque: Double
		val type: String
		val velocity: Double2
		var render: Render
		val isStatic: Boolean
		val isSleeping: Boolean

		companion object {
			fun create(options: dynamic): Body
			fun applyForce(body: Body, position: Double2, force: Double2)
			fun rotate(body: Body, rotation: Double, point: Double2 = definedExternally)
			fun setAngle(body: Body, angle: Double)

			fun setDensity(body: Body, density: Double)
			fun setInertia(body: Body, inertia: Double)
			fun setMass(body: Body, mass: Double)
			fun setPosition(body: Body, position: Double2)
			fun setVelocity(body: Body, velocity: Double2)
			fun setTranslate(body: Body, translation: Double2)
			fun setStatic(body: Body, static: Boolean)
		}
	}
}

class Render {
	var fillStyle: String = ""
	var lineWidth: Double = 0.0
	var opacity: Double = 0.0
	var sprite: Sprite = Sprite()
	var strokeStyle: String = ""
	var visible: Boolean = false

	class Sprite {
		var texture: String = ""
		var xOffset: Double = 0.0
		var xScale: Double = 0.0
		var yOffset: Double = 0.0
		var yScale: Double = 0.0
	}
}