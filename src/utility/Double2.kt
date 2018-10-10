package utility

data class Double2(var x: Double, var y: Double) {
	val sqrMagnitude
		get() = x * x + y * y

	val magnitude
		get() = kotlin.math.sqrt(sqrMagnitude)

	val normalized: Double2
		get() {
			val magnitude = magnitude
			return Double2(x / magnitude, y / magnitude)
		}

	fun difference(double2: Double2): Double2 = Double2(double2.x - this.x, double2.y - this.y)

	fun distance(double2: Double2): Double {
		return difference(double2).magnitude
	}

	operator fun minus(double2: Double2) = Double2(this.x - double2.x, this.y - double2.y)

	operator fun plus(double2: Double2) = Double2(this.x + double2.x, this.y + double2.y)


	fun dot(double2: Double2) = this.x * double2.x + this.y * double2.y
}