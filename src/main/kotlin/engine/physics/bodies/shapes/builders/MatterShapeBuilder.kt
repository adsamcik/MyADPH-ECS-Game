package engine.physics.bodies.shapes.builders

import engine.entity.Entity
import engine.physics.bodies.IBody
import engine.physics.bodies.MatterBody
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.Polygon
import engine.physics.bodies.shapes.Rectangle
import engine.physics.engines.MatterPhysicsEngine
import general.Double2
import definition.jslib.Matter

class MatterShapeBuilder(private val position: Double2,
                         private val entity: Entity,
                         private val world: Matter.World) : IShapeBuilder {

	override fun build(rectangle: Rectangle): IBody {
		val body = Matter.Bodies.rectangle(
			position.x * MatterPhysicsEngine.MATTER_SCALE,
			position.y * MatterPhysicsEngine.MATTER_SCALE,
			rectangle.width * MatterPhysicsEngine.MATTER_SCALE,
			rectangle.height * MatterPhysicsEngine.MATTER_SCALE
		)
		return buildBody(body)
	}

	override fun build(circle: Circle): IBody {
		val body = Matter.Bodies.circle(
			position.x * MatterPhysicsEngine.MATTER_SCALE,
			position.y * MatterPhysicsEngine.MATTER_SCALE,
			circle.radius * MatterPhysicsEngine.MATTER_SCALE
		)

		return buildBody(body)
	}

	override fun build(polygon: Polygon): IBody {
		val body = Matter.Bodies.fromVertices(
			position.x * MatterPhysicsEngine.MATTER_SCALE,
			position.y * MatterPhysicsEngine.MATTER_SCALE,
			polygon.points.map { it * MatterPhysicsEngine.MATTER_SCALE }.toTypedArray()
		)
		return buildBody(body)
	}

	private fun buildBody(body: Matter.Body) = MatterBody(body, entity, world)
}