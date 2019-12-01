package org.w3c.dom.events

import org.w3c.dom.Element

/**
 * Exposes the JavaScript [TouchEvent](https://developer.mozilla.org/en-US/docs/Web/API/TouchEvent) to Kotlin
 */
open external class TouchEvent : UIEvent {
	val changedTouches: TouchList
	val targetTouches: TouchList
	var touches: TouchList

	val altKey: Boolean
	val ctrlKey: Boolean
	val metaKey: Boolean
	val shiftKey: Boolean
}

external interface TouchList {
	val length: Int
}

inline fun TouchList.get(index: Int): Touch {
	return asDynamic()[index].unsafeCast<Touch>()
}

inline fun TouchList.forEach(action: (Touch) -> Unit) {
	for (i in 0 until length) {
		action(get(i))
	}
}

external interface Touch {
	val identifier: Long
	val screenX: Int
	val screenY: Int
	val pageX: Double
	val pageY: Double
	val clientX: Int
	val clientY: Int
	val target: Element

	val radiusX: Double
	val radiusY: Double
	val rotationAngle: Double
	val force: Double
}