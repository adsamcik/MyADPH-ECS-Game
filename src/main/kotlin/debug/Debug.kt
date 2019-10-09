package debug

object Debug {
	private val debugLevel: DebugLevel = DebugLevel.ALL

	fun isActive(level: DebugLevel) = level.ordinal >= debugLevel.ordinal

	fun log(level: DebugLevel, data: Any) {
		if (isActive(level)) {
			console.log(data)
		}
	}

	fun log(level: DebugLevel, vararg data: Any) {
		if (isActive(level)) {
			console.log(data)
		}
	}
}