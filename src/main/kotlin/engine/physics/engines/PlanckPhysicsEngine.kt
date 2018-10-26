package engine.physics.engines

import engine.entity.Entity
import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import engine.physics.bodies.PlanckBody
import engine.physics.events.PlanckEventManager
import jslib.planck
import utility.Double2

class PlanckPhysicsEngine : PhysicsEngine() {


	private val world = planck.World()

	override fun update(deltaMs: Int) {
		world.step(deltaMs)
	}

	override val eventManager = PlanckEventManager(world)
	override val shapeBuilder = PlanckShapeBuilder(world)

	class PlanckShapeBuilder(private val world: planck.World) : IPhysicsShapeBuilder {
		override fun build(entity: Entity, position: Double2, rectangle: Rectangle) =
			PlanckBody(buildShape(position, rectangle), entity, world)

		override fun build(entity: Entity, position: Double2, shape: Circle) =
			PlanckBody(buildShape(position, shape), entity, world)

		override fun build(entity: Entity, position: Double2, polygon: Polygon) =
			PlanckBody(buildShape(polygon), entity, world)


		fun buildShape(position: Double2, rectangle: Rectangle) =
			planck.Box(rectangle.width, rectangle.height, position.toVec2())

		fun buildShape(position: Double2, shape: Circle) = planck.Circle(position.toVec2(), shape.radius)
		fun buildShape(polygon: Polygon) = planck.Polygon(polygon.points.map { it.toVec2() }.toTypedArray())

		fun buildShape(position: Double2, shape: IShape): planck.Shape {
			return when (shape) {
				is Rectangle -> buildShape(position, shape)
				is Circle -> buildShape(position, shape)
				is Polygon -> buildShape(shape)
				else -> throw IllegalArgumentException("Shape ${shape::class.simpleName} not supported")
			}
		}

	}
}