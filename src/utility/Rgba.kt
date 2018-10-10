package utility

data class Rgba(var value: Int) {

	constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) : this(
			red.and(255).shl(24) +
					green.and(255).shl(16) +
					blue.and(255).shl(8) +
					alpha.and(255)
	)

	var red
		get() = value.shr(24)
		set(value) = setColorChannel(value, 24)

	var green
		get() = value.shr(16).and(255)
		set(value) = setColorChannel(value, 16)

	var blue
		get() = value.shr(8).and(255)
		set(value) = setColorChannel(value, 8)

	var alpha
		get() = value.and(255)
		set(value) = setColorChannel(value, 0)

	val rgbaString: String
		get() = "rgba($red,$green,$blue,$alpha)"


	private fun setColorChannel(value: Int, offset: Int) {
		if (offset > 24 || offset < 0 || offset.rem(8) != 0)
			throw IllegalArgumentException("Offset must be 0, 8, 16 or 24. Was $offset")

		val channelMask = 255.shl(offset)

		this.value = this.value.and(channelMask.inv()).or(value.toInt().shl(offset))
	}


	companion object {
		val RED
			get() = Rgba(255, 0, 0, 255)

		val GREEN
			get() = Rgba(0, 255, 0, 255)

		val BLUE
			get() = Rgba(0, 0, 255, 255)
	}

}