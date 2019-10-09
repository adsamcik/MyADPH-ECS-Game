package engine.events

import debug.Debug
import debug.DebugLevel
import kotlin.reflect.KClass

abstract class EventManager {
	private val eventListenerMap = mutableMapOf<String, EventCategory<*>>()

	abstract fun <T : IEvent> validateEventListener(eventName: String, eventType: KClass<out T>): Boolean

	private fun <T : IEvent> getCategory(eventName: String): EventCategory<T>? {
		val eventCategory = eventListenerMap[eventName] ?: return null
		return eventCategory.unsafeCast<EventCategory<T>>()
	}

	protected inline fun <reified T : IEvent> on(event: String, noinline listener: EventListener<T>) {
		on(event, listener, T::class)
	}

	private fun <T : IEvent> validateNameTypeCombination(eventName: String, eventType: KClass<out T>) {
		if (Debug.isActive(DebugLevel.IMPORTANT)) {
			require(validateEventListener(eventName, eventType)) {
				"Invalid name $eventName type $eventType combination."
			}
		}
	}

	protected fun <T : IEvent> on(eventName: String, listener: EventListener<T>, eventType: KClass<out T>) {
		validateNameTypeCombination(eventName, eventType)

		val category = getCategory(eventName) ?: EventCategory(eventType).also {
			eventListenerMap[eventName] = it
		}

		category.listen(listener)
	}

	protected fun <T : IEvent> removeListener(eventName: String, listener: EventListener<T>) {
		val category = getCategory<T>(eventName)
		if (category == null) {
			Debug.log(DebugLevel.IMPORTANT) { "Listener for event $eventName does not exist." }
		} else {
			category.removeListener(listener)
		}
	}

	protected fun <T : IEvent> emit(eventName: String, event: T) {
		getCategory<T>(eventName)?.emit(event)
	}

	protected class EventCategory<T : IEvent>(val type: KClass<T>) {
		private val listenerList = mutableListOf<EventListener<T>>()
		private val onceList = mutableListOf<EventListener<T>>()

		fun listen(listener: EventListener<T>) {
			listenerList.add(listener)
		}

		fun listenOnce(listener: EventListener<T>) {
			onceList.add(listener)
		}

		fun removeListener(listener: EventListener<T>) {
			listenerList.remove(listener)
		}

		fun removeOnceListener(listener: EventListener<T>) {
			onceList.remove(listener)
		}

		fun emit(event: T) {
			listenerList.forEach { it(event) }
			onceList.forEach { it(event) }
			onceList.clear()
		}
	}
}

typealias EventListener<T> = (event: T) -> Unit

interface IEvent