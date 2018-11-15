package extensions

object MathExtensions {
	private const val DEG_TO_RAD = kotlin.math.PI / 180.0
	private const val RAD_TO_DEG = 180.0 / kotlin.math.PI

	fun toRadians(degrees: Double) = degrees * DEG_TO_RAD

	fun toDegrees(radians: Double) = radians * RAD_TO_DEG
}