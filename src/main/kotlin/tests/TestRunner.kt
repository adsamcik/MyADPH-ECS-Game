package tests

import debug.Debug
import debug.DebugLevel
import engine.Core

class TestRunner {
	private val testList = listOf(Double2Test(), RgbaTest(), BodyTest())

	private fun initializeSingletons() {
		Core
	}

	fun run() {
		if (Debug.isActive(DebugLevel.CRITICAL)) {
			initializeSingletons()

			console.log("Running tests")
			testList.forEach {
				console.log("Running ${it::class.simpleName}")
				it.run()
			}
			console.log("All tests ran successfully")
		}
	}
}