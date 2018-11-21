package tests

import engine.input.Input
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.browser.document

class BodyTest : ITest {
	override fun run() {
		initialize()
	}

	private fun initialize() {
		val event = KeyboardEvent("keydown", KeyboardEventInit(key = "a", code = "KeyA"))
		document.dispatchEvent(event)
		Input.update(1.0/60.0)

		Assert.isTrue(Input.left.isDown())
	}

}