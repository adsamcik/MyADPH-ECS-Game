package engine.physics.bodies.shapes.builders

import engine.physics.bodies.IBody
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.Polygon
import engine.physics.bodies.shapes.Rectangle

interface IShapeBuilder {
	fun build(rectangle: Rectangle): IBody
	fun build(circle: Circle): IBody
	fun build(polygon: Polygon): IBody
}