package tests

import engine.input.Input
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import kotlin.browser.document

class BodyTest : ITest {
	override fun run() {
		initialize()
		finalize()
	}

	private fun initialize() {
		dispatchEvent(EventType.KeyDown, 'w')
		Input.update(1.0/60.0)

		Assert.isTrue(Input.up.isDown())
	}

	private fun finalize() {
		//Input.update(1.0/60.0)

		dispatchEvent(EventType.KeyUp, 'w')

		Input.update(1.0/60.0)
	}

	private fun dispatchEvent(type: EventType, key: Char) {
		val event = KeyboardEvent(type.toString(), KeyboardEventInit(key = key.toString(), code = "Key${key.toUpperCase()}"))
		document.dispatchEvent(event)
	}

	private enum class EventType {
		KeyDown {
			override fun toString() = "keydown"
		},
		KeyUp {
			override fun toString() = "keyup"
		}
	}

}