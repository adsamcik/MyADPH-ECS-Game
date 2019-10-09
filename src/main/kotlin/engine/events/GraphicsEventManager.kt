package engine.events

import general.Int2
import org.w3c.dom.events.Event
import kotlin.browser.window
import kotlin.reflect.KClass

object GraphicsEventManager : EventManager() {
	private const val RESIZE = "resize"

	override fun <T : IEvent> validateEventListener(eventName: String, eventType: KClass<out T>): Boolean {
		return when (eventName) {
			RESIZE -> eventType == ResizeEventData::class
			else -> false
		}
	}

	fun onResize(listener: EventListener<ResizeEventData>) {
		on(RESIZE, listener)
	}

	fun removeOnResize(listener: EventListener<ResizeEventData>) {
		removeOnResize(listener)
	}

	init {
		window.onresize = this::onResize
	}

	private fun onResize(event: Event? = null) {
		emit(RESIZE, ResizeEventData(Int2(window.innerWidth, window.innerHeight)))
	}
}

data class ResizeEventData(val dimensions: Int2) : IEvent