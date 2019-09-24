package engine.types

import kotlinx.serialization.*
import kotlin.math.roundToInt

@ExperimentalUnsignedTypes
@Serializable(with = RgbaSerializer::class)
data class Rgba(var value: UInt) {

	constructor(red: UInt, green: UInt, blue: UInt, alpha: UInt = 255U) : this(
		(red.and(255U).shl(24) +
				green.and(255U).shl(16) +
				blue.and(255U).shl(8) +
				alpha.and(255U)).toUInt()
	)

	constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) : this(
		red.toUInt(),
		green.toUInt(),
		blue.toUInt(),
		alpha.toUInt()
	)

	constructor(value: Int) : this(value.toUInt())

	var red
		get() = value.shr(24)
		set(value) = setColorChannel(value, 24)

	var redDouble: Double
		get() = uIntColorComponentToDouble(red)
		set(value) {
			red = doubleColorComponentToUInt(value)
		}

	var green
		get() = value.shr(16).and(255U)
		set(value) = setColorChannel(value, 16)

	var greenDouble: Double
		get() = uIntColorComponentToDouble(green)
		set(value) {
			green = doubleColorComponentToUInt(value)
		}

	var blue
		get() = value.shr(8).and(255U)
		set(value) = setColorChannel(value, 8)

	var blueDouble: Double
		get() = uIntColorComponentToDouble(blue)
		set(value) {
			blue = doubleColorComponentToUInt(value)
		}

	var alpha
		get() = value.and(255U)
		set(value) = setColorChannel(value, 0)

	var alphaDouble: Double
		get() = uIntColorComponentToDouble(alpha)
		set(value) {
			alpha = doubleColorComponentToUInt(value)
		}

	val rgbaString: String
		get() = "rgba($red,$green,$blue,$alpha)"

	val rgb: Int
		get() = uRgb.toInt()

	val uRgb: UInt
		get() = value.and(255U.inv()).shr(8)

	val rgba: Int
		get() = value.toInt()

	val uRgba: UInt
		get() = value


	private fun uIntColorComponentToDouble(component: UInt): Double {
		val coerced = component.toInt().coerceIn(0, 255)
		return coerced.toDouble() / 255.0
	}

	private fun doubleColorComponentToUInt(component: Double): UInt {
		val coerced = component.coerceIn(0.0, 1.0)
		return (coerced * 255.0).roundToInt().toUInt()
	}


	private fun setColorChannel(value: UInt, offset: Int) {
		require(!(offset > 24 || offset < 0 || offset.rem(8) != 0)) { "Offset must be 0, 8, 16 or 24. Was $offset" }

		val channelMask = 255U.shl(offset)

		this.value = this.value.and(channelMask.inv()).or(value.shl(offset))
	}


	companion object {
		val NONE
			get() = Rgba(0U, 0U, 0U, 0U)

		val RED
			get() = Rgba(255U, 0U, 0U)

		val GREEN
			get() = Rgba(0U, 255U, 0U)

		val BLUE
			get() = Rgba(0U, 0U, 255U)

		val BLACK
			get() = Rgba(0U, 0U, 0U)

		val WHITE
			get() = Rgba(255U, 255U, 255U)

		val YELLOW
			get() = Rgba(255U, 255U, 0U)

		val PINK
			get() = Rgba(255U, 0U, 255U)

		val CYAN
			get() = Rgba(0U, 255U, 255U)

		val GRAY
			get() = Rgba(127U, 127U, 127U)

		val FOREST_GREEN
			get() = Rgba(34U, 139U, 34U)

		val SKY_BLUE
			get() = Rgba(135U, 206U, 235U)

		val ORANGE
			get() = Rgba(255U, 114U, 81U)
	}

}

@Serializer(forClass = Rgba::class)
object RgbaSerializer : KSerializer<Rgba> {
	override val descriptor: SerialDescriptor
		get() = throw NotImplementedError()

	override fun serialize(encoder: Encoder, obj: Rgba) {
		encoder.encodeLong(obj.value.toLong())
	}

	override fun deserialize(decoder: Decoder): Rgba {
		return Rgba(decoder.decodeLong().toUInt())
	}
}