package engine.physics.bodies.shapes.builders

import engine.entity.Entity
import engine.physics.bodies.IBody
import engine.physics.bodies.PlanckBody
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.Polygon
import engine.physics.bodies.shapes.Rectangle
import general.Double2
import jslib.planck

class PlanckShapeBuilder(private val position: Double2,
                         private val entity: Entity,
                         private val world: planck.World) : IShapeBuilder {

	override fun build(rectangle: Rectangle): IBody {
		val shape = planck.Box(rectangle.width / 2, rectangle.height / 2)
		return buildBody(shape)
	}

	override fun build(circle: Circle): IBody {
		val shape = planck.Circle(circle.radius)
		return buildBody(shape)
	}

	override fun build(polygon: Polygon): IBody {
		val shape = planck.Polygon(polygon.points.map { it.toVec2() }.toTypedArray())
		return buildBody(shape)
	}

	private fun buildBody(shape: planck.Shape) = PlanckBody(shape, position, entity, world)

}