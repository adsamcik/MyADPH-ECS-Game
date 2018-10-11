package engine.physics

import utility.Double2

//pattern double dispatch
interface IShape

data class Circle(val radius: Double) : IShape

data class Rectangle(val width: Double, val height: Double) : IShape
data class Polygon(val points: Collection<Double2>) : IShape