package general

data class Int2(var x: Int = 0, var y: Int = x) {
	operator fun div(double2: Double2) = Double2(this.x.toDouble() / double2.x, this.y.toDouble() / double2.y)

	operator fun div(double: Double) = Double2(this.x.toDouble() / double, this.y.toDouble() / double)

	operator fun div(integer: Int) = Int2(this.x / integer, this.y / integer)

	operator fun minus(int2: Int2) = Int2(this.x - int2.x, this.y - int2.y)

	operator fun plus(int2: Int2) = Int2(this.x - int2.x, this.y - int2.y)

	fun toDouble2() = Double2(x.toDouble(), y.toDouble())
}