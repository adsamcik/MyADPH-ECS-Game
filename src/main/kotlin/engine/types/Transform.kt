package engine.types

import extensions.MathExtensions
import extensions.toDegrees
import extensions.toRadians
import general.Double2
import kotlinx.serialization.Serializable

@Serializable
data class Transform(var position: Double2, var angleRadians: Double) {
	var angleDegrees
		get() = angleRadians.toDegrees()
		set(value) {
			angleRadians = value.toRadians()
		}
}