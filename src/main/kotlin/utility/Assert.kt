package utility

object Assert {
	fun <T> equals(first: T, second: T, message: String? = null) {
		if (first != second)
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