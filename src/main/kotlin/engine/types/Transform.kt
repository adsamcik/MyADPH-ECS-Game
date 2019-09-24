package engine.types

import extensions.MathExtensions
import general.Double2
import kotlinx.serialization.Serializable

@Serializable
data class Transform(var position: Double2, var angleRadians: Double) {
	var angleDegrees
		get() = MathExtensions.toDegrees(angleRadians)
		set(value) {
			angleRadians = MathExtensions.toRadians(value)
		}
}