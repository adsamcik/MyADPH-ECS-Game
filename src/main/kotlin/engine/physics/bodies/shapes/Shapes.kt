package engine.physics.bodies.shapes

import engine.physics.bodies.IBody
import engine.physics.bodies.shapes.builders.IShapeBuilder
import general.Double2
import kotlinx.serialization.Serializable

@Serializable(with = ShapeSerializer::class)
interface IShape {
	fun duplicate(): IShape
	fun build(builder: IShapeBuilder): IBody
}

@Serializable
data class Circle(val radius: Double) : IShape {
	constructor(radius: Number) : this(radius.toDouble())
	override fun duplicate() = copy()
	override fun build(builder: IShapeBuilder) = builder.build(this)
}

@Serializable
data class Rectangle(val width: Double, val height: Double) : IShape {
	constructor(width: Number, height: Number) : this(width.toDouble(), height.toDouble())
	override fun duplicate() = copy()
	override fun build(builder: IShapeBuilder) = builder.build(this)
}

@Serializable
data class Polygon(val points: Collection<Double2>) : IShape {
	override fun duplicate() = copy()
	override fun build(builder: IShapeBuilder) = builder.build(this)
}