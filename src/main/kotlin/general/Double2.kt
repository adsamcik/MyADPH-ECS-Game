package general

import definition.jslib.Matter
import definition.jslib.pixi.IPoint
import definition.jslib.pixi.ObservablePoint
import definition.jslib.planck
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class Double2(var x: Double = 0.0, var y: Double = 0.0) {
	constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())
	constructor(vector: Matter.Vector) : this(vector.x, vector.y)
	constructor(vec2: planck.Vec2) : this(vec2.x.toDouble(), vec2.y.toDouble())

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

	fun toVec2() = planck.Vec2(x, y)

	fun toInt2() = Int2(x.toInt(), y.toInt())

	fun roundToInt2() = Int2(x.roundToInt(), y.roundToInt())

	fun coerceAtMost(value: Double) {
		this.x = this.x.coerceAtMost(value)
		this.y = this.y.coerceAtMost(value)
	}

	override fun toString(): String = "{x: $x, y: $y}"

	companion object {
		fun Matter.Vector.toDouble2() = Double2(x, y)

		fun IPoint.set(value: Double2) {
			this.set(value.x, value.y)
		}

	}

}