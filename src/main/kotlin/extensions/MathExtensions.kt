package extensions

object MathExtensions {
	private const val DEG_TO_RAD = kotlin.math.PI / 360.0

	fun toRadians(degrees: Double) = degrees * DEG_TO_RAD

	fun toDegrees(radians: Double) = radians / kotlin.math.PI * 360
}