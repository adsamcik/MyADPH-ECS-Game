package engine.physics.engines

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.Polygon
import engine.physics.Rectangle
import engine.physics.bodies.MatterBody
import engine.physics.events.MatterEventManager
import engine.physics.events.PhysicsEventManager
import jslib.Matter
import utility.Double2

class MatterPhysicsEngine : PhysicsEngine() {
	val engine: Matter.Engine = Matter.Engine.create()

	override val shapeBuilder: IPhysicsShapeBuilder = MatterShapeBuilder(this)

	val world: Matter.World
		get() = engine.world

	override val eventManager: PhysicsEventManager = MatterEventManager(engine)


	init {
		engine.enableSleeping = true
	}

	override fun update(deltaMs: Int) {
		Matter.Engine.update(engine, deltaMs.toDouble())
	}


	class MatterShapeBuilder(private val engine: MatterPhysicsEngine) : IPhysicsShapeBuilder {
		override fun build(entity: Entity, position: Double2, rectangle: Rectangle) = MatterBody(
			Matter.Bodies.rectangle(position.x, position.y, rectangle.width, rectangle.height),
			entity,
			engine.world
		)

		override fun build(entity: Entity, position: Double2, shape: Circle) =
			MatterBody(Matter.Bodies.circle(position.x, position.y, shape.radius), entity, engine.world)

		override fun build(entity: Entity, position: Double2, polygon: Polygon) =
			MatterBody(
				Matter.Bodies.fromVertices(position.x, position.y, polygon.points.toTypedArray()),
				entity,
				engine.world
			)

	}
}