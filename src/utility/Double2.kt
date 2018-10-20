package utility

import jslib.Matter

data class Double2(var x: Double, var y: Double) {
	constructor() : this(0.0, 0.0)
	constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

	val sqrMagnitude
		get() = x * x + y * y

	val magnitude
		get() = kotlin.math.sqrt(sqrMagnitude)

	val normalized: Double2
		get() {
			val magnitude = magnitude
			return Double2(x / magnitude, y / magnitude)
		}

	val normalVector: Double2
		get() = Double2(y, -x)

	fun difference(double2: Double2): Double2 = Double2(double2.x - this.x, double2.y - this.y)

	fun distance(double2: Double2): Double {
		return difference(double2).magnitude
	}

	operator fun minus(double2: Double2) = Double2(this.x - double2.x, this.y - double2.y)

	operator fun unaryMinus() = Double2(-this.x, -this.y)

	operator fun plus(double2: Double2) = Double2(this.x + double2.x, this.y + double2.y)

	operator fun times(double2: Double2) = Double2(this.x * double2.x, this.y * double2.y)

	operator fun times(double: Double) = Double2(this.x * double, this.y * double)

	operator fun Double.times(double2: Double2) = double2.times(this)

	operator fun div(double2: Double2) = Double2(this.x / double2.x, this.y / double2.y)

	operator fun div(double: Double) = Double2(this.x / double, this.y / double)


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