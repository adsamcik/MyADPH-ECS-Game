package jslib

import org.w3c.dom.events.MouseEvent


inline val MouseEvent.movementX: Double
	get() = asDynamic().movementX as Double

inline val MouseEvent.movementY: Double
	get() = asDynamic().movementY as Double
