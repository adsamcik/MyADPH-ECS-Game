package engine.physics

import jslib.Matter
import utility.Double2


interface IShape {
	fun buildBody(position: Double2): Matter.Body
}

data class Circle(val radius: Double) : IShape {
	override fun buildBody(position: Double2): Matter.Body = Matter.Bodies.circle(position.x, position.y, radius)
}


data class Rectangle(val width: Double, val height: Double) : IShape {
	override fun buildBody(position: Double2): Matter.Body = Matter.Bodies.rectangle(position.x, position.y, width, height)
}

data class Polygon(val points: Collection<Double2>) : IShape {
	override fun buildBody(position: Double2): Matter.Body = Matter.Bodies.fromVertices(position.x, position.y, points.toTypedArray())
}