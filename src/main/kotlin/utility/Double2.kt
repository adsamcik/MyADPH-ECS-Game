package utility

import jslib.Matter
import jslib.pixi.ObservablePoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Double2(var x: Double, var y: Double) {
	constructor() : this(0.0, 0.0)
	constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

	@Transient
	val sqrMagnitude
		get() = x * x + y * y

	@Transient
	val magnitude
		get() = kotlin.math.sqrt(sqrMagnitude)

	@Transient
	val normalized: Double2
		get() {
			val magnitude = magnitude
			return Double2(x / magnitude, y / magnitude)
		}

	@Transient
	val normalVector: Double2
		get() = Double2(y, -x)

	fun difference(double2: Double2): Double2 = Double2(double2.x - this.x, double2.y - this.y)

	fun distance(double2: Double2): Double {
		return difference(double2).magnitude
	}

	operator fun minus(double2: Double2) = Double2(this.x - double2.x, this.y - double2.y)

	operator fun minus(point: ObservablePoint) = Double2(this.x - point.x.toDouble(), this.y - point.y.toDouble())

	operator fun minus(vector: Matter.Vector) = Double2(this.x - vector.x, this.y - vector.y)

	operator fun unaryMinus() = Double2(-this.x, -this.y)

	operator fun plus(double2: Double2) = Double2(this.x + double2.x, this.y + double2.y)

	operator fun plus(point: ObservablePoint) = Double2(this.x + point.x.toDouble(), this.y + point.y.toDouble())

	operator fun plus(vector: Matter.Vector) = Double2(this.x + vector.x, this.y + vector.y)

	operator fun times(double2: Double2) = Double2(this.x * double2.x, this.y * double2.y)

	operator fun times(double: Double) = Double2(this.x * double, this.y * double)

	operator fun Double.times(double2: Double2) = double2.times(this)

	operator fun div(double2: Double2) = Double2(this.x / double2.x, this.y / double2.y)

	operator fun div(double: Double) = Double2(this.x / double, this.y / double)

	operator fun div(integer: Int) = div(integer.toDouble())


	fun dot(double2: Double2) = this.x * double2.x + this.y * double2.y


	fun toVector() = Matter.Vector.create(x, y)

	fun coerceAtMost(value: Double) {
		this.x = this.x.coerceAtMost(value)
		this.y = this.y.coerceAtMost(value)
	}

	override fun toString(): String = "{x: $x, y: $y}"

	companion object {
		fun Matter.Vector.toDouble2() = Double2(x, y)
	}

}