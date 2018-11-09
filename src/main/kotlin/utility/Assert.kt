package utility

object Assert {
	fun <T> equals(expected: T, actual: T, message: String? = null) {
		if (expected != actual)
			throw AssertionError(message)
	}

	fun isTrue(value: Boolean, message: String? = null) {
		if (!value)
			throw AssertionError(message)
	}

	fun <T> isNotNull(value: T?, message: String? = null) {
		if (value == null)
			throw AssertionError(message)
	}

	fun <T> isNull(value: T?, message: String? = null) {
		if (value != null)
			throw AssertionError(message)
	}
}