package tests

import engine.types.Rgba

class RgbaTest : ITest {
	override fun run() {
		baseTest()
		randomTest()
	}

	private fun baseTest() {
		Assert.equals(0xFF0000, Rgba.RED.rgb)
		Assert.equals(0x00FF00, Rgba.GREEN.rgb)
		Assert.equals(0x0000FF, Rgba.BLUE.rgb)
	}

	private fun randomTest() {
		val rng = kotlin.random.Random(0xF0F0F0)
		for (i in 0..10000) {
			val value = rng.nextInt()
			val color = Rgba(value)
			Assert.equals(value, color.rgba)
		}
	}
}