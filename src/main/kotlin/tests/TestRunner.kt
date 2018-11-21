package tests

import engine.Core

class TestRunner {
	val testList = mutableListOf<ITest>()

	init {
		testList.add(Double2Test())
		testList.add(RgbaTest())
		testList.add(BodyTest())
	}


	fun run() {
		//Initialize singletons
		Core

		console.log("Running tests")
		testList.forEach {
			console.log("Running ${it::class.simpleName}")
			it.run()
		}
		console.log("All tests ran successfully")
	}
}