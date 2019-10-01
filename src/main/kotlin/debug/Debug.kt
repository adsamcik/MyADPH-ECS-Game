package debug

object Debug {
	private val debugLevel: DebugLevel = DebugLevel.ALL

	fun shouldLog(level: DebugLevel) = level.ordinal >= debugLevel.ordinal

	fun log(level: DebugLevel, data: Any) {
		if (shouldLog(level)) {
			console.log(data)
		}
	}

	fun log(level: DebugLevel, vararg data: Any) {
		if (shouldLog(level)) {
			console.log(data)
		}
	}
}