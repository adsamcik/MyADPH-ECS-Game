package tests

object Assert {
	fun <T> equals(expected: T, actual: T, message: String? = null) {
		if (expected != actual)
			throw AssertionError("Expected $expected but got $actual. $message")
	}

	fun isTrue(value: Boolean, message: String? = null) {
		if (!value)
			throw AssertionError("Expected true but was false. $message")
	}

	fun <T> isNotNull(value: T?, message: String? = null) {
		if (value == null)
			throw NullPointerException("Expected value but found null. $message")
	}

	fun <T> isNull(value: T?, message: String? = null) {
		if (value != null)
			throw AssertionError("Expected null but found value. $message")
	}
}