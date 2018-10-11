import utility.Double2

open external class Matter {
	open class Engine {
		var enableSleeping: Boolean

		companion object {
			fun create(): Engine
			fun run(engine: Engine)
			fun update(engine: Engine, delta: Double = definedExternally, correction: Double = definedExternally)
		}
	}

	open class World {
		companion object {
			fun add(world: World, obj: dynamic): Composite
		}
	}

	open class Composite {
		val id: Int
		val bodies: Array<Body>
		val composites: Array<Composite>

		companion object {
			fun add(composite: Composite, obj: dynamic): Composite
			fun allBodies(composite: Composite): Array<Body>
		}
	}

	open class Body {
		val vertices: Array<Double2>

		companion object {
			fun create(options: dynamic): Body
			fun applyForce(body: Body, position: Double2, force: Double2)
			fun rotate(body: Body, rotation: Double, point: Double2 = definedExternally)
		}
	}
}

