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

open external class TouchList {
	val length: Int
	fun item(index: Int): TouchEvent
}

open external class Touch {
	open val identifier: String
	open val screenX: Int
	open val screenY: Int
	open val pageX: Double
	open val pageY: Double
	open val clientX: Int
	open val clientY: Int
	open val target: Element

	open val radiusX: Double
	open val radiusY: Double
	open val rotationAngle: Double
	open val force: Double
}