package utility

import kotlinx.serialization.*

@ExperimentalUnsignedTypes
@Serializable(with = RgbaSerializer::class)
data class Rgba(var value: UInt) {

	constructor(red: UInt, green: UInt, blue: UInt, alpha: UInt = 255U) : this(
			(red.and(255U).shl(24) +
					green.and(255U).shl(16) +
					blue.and(255U).shl(8) +
					alpha.and(255U)).toUInt()
	)

	constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) : this(red.toUInt(), green.toUInt(), blue.toUInt(), alpha.toUInt())

	var red
		get() = value.shr(24)
		set(value) = setColorChannel(value, 24)

	var green
		get() = value.shr(16).and(255U)
		set(value) = setColorChannel(value, 16)

	var blue
		get() = value.shr(8).and(255U)
		set(value) = setColorChannel(value, 8)

	var alpha
		get() = value.and(255U)
		set(value) = setColorChannel(value, 0)

	val rgbaString: String
		get() = "rgba($red,$green,$blue,$alpha)"

	val rgb: Int
		get() = value.and(255U.inv()).shr(8).toInt()


	private fun setColorChannel(value: UInt, offset: Int) {
		if (offset > 24 || offset < 0 || offset.rem(8) != 0)
			throw IllegalArgumentException("Offset must be 0, 8, 16 or 24. Was $offset")

		val channelMask = 255U.shl(offset)

		this.value = this.value.and(channelMask.inv()).or(value.shl(offset))
	}


	companion object {
		val RED
			get() = Rgba(255, 0, 0)

		val GREEN
			get() = Rgba(0, 255, 0)

		val BLUE
			get() = Rgba(0, 0, 255)

		val BLACK
			get() = Rgba(0, 0, 0)

		val WHITE
			get() = Rgba(255, 255, 255)
	}

}

@Serializer(forClass = Rgba::class)
object RgbaSerializer{
	override fun serialize(output: Encoder, obj: Rgba) {
		output.encodeLong(obj.value.toLong())
	}

	override fun deserialize(input: Decoder): Rgba {
		return Rgba(input.decodeLong().toUInt())
	}
}