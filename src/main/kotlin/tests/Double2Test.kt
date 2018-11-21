package tests

import general.Double2

class Double2Test : ITest {
	override fun run() {
		testValue()
		testAddition()
		testMultiplication()
	}

	private fun testValue() {
		val double2 = Double2(5.0, 3.0)
		Assert.equals(5.0, double2.x)
		Assert.equals(3.0, double2.y)
	}

	private fun testAddition() {
		val x = 5.0
		val y = 3.0
		val double2 = Double2(x, y)

		val addition = double2 + double2
		Assert.equals(x, double2.x)
		Assert.equals(y, double2.y)

		Assert.equals(x + x, addition.x)
		Assert.equals(y + y, addition.y)
	}

	private fun testMultiplication() {
		val x = 5.0
		val y = 3.0
		val double2 = Double2(x, y)

		val multiplication = double2 * double2
		Assert.equals(x, double2.x)
		Assert.equals(y, double2.y)

		Assert.equals(x * x, multiplication.x)
		Assert.equals(y * y, multiplication.y)
	}
}