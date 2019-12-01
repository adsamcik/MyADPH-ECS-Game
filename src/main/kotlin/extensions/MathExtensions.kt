package extensions

import general.Double2
import kotlin.math.cos
import kotlin.math.sin

object MathExtensions {
	const val DEG_TO_RAD = kotlin.math.PI / 180.0
	const val RAD_TO_DEG = 180.0 / kotlin.math.PI
}

fun Double.toRadians() = this * MathExtensions.DEG_TO_RAD
fun Double.toDegrees() = this * MathExtensions.RAD_TO_DEG

fun Double.radiansToVector() = Double2(cos(this), sin(this))
fun Double.degreesToVector() = toRadians().radiansToVector()