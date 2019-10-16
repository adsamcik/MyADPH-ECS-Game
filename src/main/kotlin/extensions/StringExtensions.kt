package extensions

fun String.format(): String {
	val builder = StringBuilder(length + length / 5)
	var lastUpperCase = 0
	for (i in 0 until length) {
		val character = get(i)
		if (character.toUpperCase() == character) {
			if (lastUpperCase < i - 1) {
				builder
					.append(' ')
					.append(get(lastIndex).toLowerCase())
					.append(substring(lastIndex + 1, i))
			}
			lastUpperCase = i
		}
	}

	builder.append(get(lastIndex).toLowerCase()).append(substring(lastIndex + 1, length))

	return builder.trim().toString()
}