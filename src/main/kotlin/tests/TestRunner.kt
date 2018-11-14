package tests

class TestRunner {
	val testList = mutableListOf<ITest>()

	init {
		testList.add(Double2Test())
		testList.add(RgbaTest())
	}


	fun run() {
		console.log("Running tests")
		testList.forEach {
			console.log("Running ${it::class.simpleName}")
			it.run()
		}
		console.log("All tests ran successfully")
	}
}