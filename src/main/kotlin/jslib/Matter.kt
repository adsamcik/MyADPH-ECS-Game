package jslib

import engine.entity.Entity
import utility.Double2
import kotlin.reflect.KFunction1

@JsModule("matter-js")
@JsNonModule

abstract external class Matter {
	val version: String
	val name: String

	fun use(plugins: Array<String>)

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
		val bounds: Bounds
		val gravity: Gravity

		companion object {
			fun add(world: World, obj: dynamic): Composite
			fun remove(world: World, obj: dynamic, deep: Boolean = definedExternally): Composite
		}

		open class Gravity {
			var scale: Double = definedExternally
			var x: Double = definedExternally
			var y: Double = definedExternally
		}
	}

	abstract class Composite {
		val id: Int
		val bodies: Array<Body>
		val composites: Array<Composite>

		companion object {
			fun add(composite: Composite, obj: dynamic): Composite
			fun allBodies(composite: Composite): Array<Body>
			fun remove(composite: Composite, obj: dynamic, deep: Boolean = definedExternally): Composite
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
		val vertices: Array<Number>
		val sleepThreshold: Int
		val slop: Number
		val speed: Number
		val timeScale: Number
		val type: String
		val velocity: Vector
		val isStatic: Boolean
		val isSleeping: Boolean
		val position: Vector
		val mass: Number
		val inverseMass: Number
		val motion: Number
		val density: Number
		val collisionFilter: CollisionFilter
		val angularVelocity: Number
		val angularSpeed: Number


		val parent: Body
		val parts: Array<Body>

		var torque: Number
		var restitution: Number
		var render: BodyRender
		var friction: Number
		var frictionAir: Number
		var frictionStatic: Number
		var angle: Number
		var isSensor: Boolean


		//extra payload
		var entity: Entity

		companion object {
			fun create(options: dynamic): Body
			fun applyForce(body: Body, position: Vector, force: Vector)
			fun rotate(body: Body, rotation: Number, point: Vector = definedExternally)
			fun setAngle(body: Body, angle: Number)

			fun setDensity(body: Body, density: Number)
			fun setInertia(body: Body, inertia: Number)
			fun setMass(body: Body, mass: Number)
			fun setPosition(body: Body, position: Vector)
			fun setVelocity(body: Body, velocity: Vector)
			fun setTranslate(body: Body, translation: Vector)
			fun setStatic(body: Body, static: Boolean)
			fun setVertices(body: Body, vertices: Array<Number>)
		}

		abstract class CollisionFilter {
			var category: Int
			var group: Int
			var mask: Int
		}
	}




	abstract class Pair

	abstract class Sleeping {
		companion object {
			fun afterCollisions(pairs: Array<Pair>, timeScale: Double)
			fun set(body: Body, isSleeping: Boolean)
			fun update(bodies: Array<Body>, timeScale: Double)
		}
	}

	abstract class Bounds {
		val min: Vector
		val max: Vector

		companion object {
			fun contains(bounds: Bounds, point: Vector): Boolean
			fun create(vertices: Vertices): Bounds
			fun overlaps(boundsA: Bounds, boundsB: Bounds): Boolean
			fun shift(bounds: Bounds, position: Vector)
			fun translate(bounds: Bounds, vector: Vector)
			fun update(bounds: Bounds, vertices: Vertices, velocity: Vector)
		}
	}

	abstract class Vertices

	object Events {
		fun off(obj: dynamic, eventNames: String, callback: KFunction1<dynamic, Unit>)
		fun on(obj: dynamic, eventNames: String, callback: KFunction1<dynamic, Unit>)
		fun trigger(obj: dynamic, eventNames: String, callback: KFunction1<dynamic, Unit>)
	}

	abstract class Render {
		companion object {
			fun create(options: dynamic): Render
			fun run(render: Render)
		}
	}

	open class Vector {
		var x: Double
		var y: Double

		companion object {
			fun add(vectorA: Vector, vectorB: Vector, output: Vector = definedExternally): Vector
			fun angle(vectorA: Vector, vectorB: Vector): Double
			fun mult(vectorA: Vector, scalar: Number): Vector
			fun normalise(vector: Vector): Vector
			fun create(x: Double, y: Double): Vector
		}
	}
}

class BodyRender {
	var fillStyle: String = ""
	var lineWidth: Double = 0.0
	var opacity: Double = 1.0
	var sprite: Sprite? = null
	var strokeStyle: String = ""
	var visible: Boolean = true

	class Sprite {
		var texture: String = ""
		var xOffset: Double = 0.0
		var xScale: Double = 0.0
		var yOffset: Double = 0.0
		var yScale: Double = 0.0
	}
}

external class CollisionEvent {
	val name: String
	val pairs: Array<PairData>
	val source: dynamic

	class PairData {
		val id: String

		val bodyA: Matter.Body
		val bodyB: Matter.Body

		val contacts: Array<Contact>

		val collision: dynamic //CollisionData

		val timeCreated: Double
		val timeUpdated: Double
		val slop: Double
		val seperation: Double
		val restitution: Double
		val isActive: Boolean
		val isSensor: Boolean
		val inverseMass: Double


		class Contact {
			val id: String
			val normalImpulse: Double
			val tangentImpulse: Double
		}

		class CollisionData {
			//todo
		}
	}
}

external class Vertex {
	val x: Double
	val y: Double
	val body: Matter.Body
	val isInternal: Boolean
	val index: Int
}