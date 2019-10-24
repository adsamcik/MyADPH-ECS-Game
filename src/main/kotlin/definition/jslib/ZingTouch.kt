package definition.jslib

import org.w3c.dom.Element
import org.w3c.dom.events.Event
import general.Double2

@JsModule("ZingTouch")
@JsNonModule


open external class ZingTouch {
	open class Region(element: Element, capture: Boolean = definedExternally, preventDefault: Boolean = definedExternally) {
		val capture: Boolean
		val element: Element
		val id: Number
		val preventDefault: Boolean

		fun bind(element: Element, gesture: ZingTouch.Gesture = definedExternally, handler: () -> Unit = definedExternally, capture: Boolean = definedExternally, bindOnce: Boolean = definedExternally): Chainable
		//fun bind(element: Element, gesture: String = definedExternally, handler: () -> Unit = definedExternally, capture: Boolean = definedExternally, bindOnce: Boolean = definedExternally): Chainable

		fun bindOnce(element: Element, gesture: ZingTouch.Gesture = definedExternally, handler: () -> Unit = definedExternally, capture: Boolean = definedExternally): Chainable
		//fun bindOnce(element: Element, gesture: String = definedExternally, handler: () -> Unit = definedExternally, capture: Boolean = definedExternally): Chainable


		class Chainable {
			fun tap(event: (GestureEvent<Tap.EventData>) -> Unit, capture: Boolean = definedExternally): Chainable
			fun swipe(event: (GestureEvent<Swipe.EventData>) -> Unit, capture: Boolean = definedExternally): Chainable
			fun pinch(event: (GestureEvent<Pinch.EventData>) -> Unit, capture: Boolean = definedExternally): Chainable
			fun expand(event: (GestureEvent<Expand.EventData>) -> Unit, capture: Boolean = definedExternally): Chainable
			fun pan(event: (GestureEvent<Pan.EventData>) -> Unit, capture: Boolean = definedExternally): Chainable
			fun rotate(event: (GestureEvent<Rotate.EventData>) -> Unit, capture: Boolean = definedExternally): Chainable
		}
	}

	open class State(regionId: String) {
		val regionId: String
		val numGestures: Int
		val registeredGestures: Map<String, ZingTouch.Gesture>
	}

	open class Gesture {
		val id: String?
		val type: String

		fun setId(id: String)
		fun getId(): String
	}

	open class Tap(options: ZingTouchExtra.TapOptions) {
		val maxDelay: Int
		val minDelay: Int
		val numInputs: Int
		val tolerance: Int

		open class EventData {

		}
	}

	open class Pan(options: ZingTouchExtra.PanOptions = definedExternally) : Gesture {
		open class EventData {
			val change: Double2
			val currentDirection: Double
			val directionFromOrigin: Double
			val distanceFromOrigin: Double
		}
	}

	open class Pinch(options: dynamic = definedExternally) : Gesture {
		open class EventData {

		}
	}

	open class Distance(options: dynamic = definedExternally) : Gesture {
		open class EventData {

		}
	}

	open class Expand(options: dynamic = definedExternally): Gesture {
		open class EventData {

		}
	}

	open class Rotate(options: dynamic = definedExternally) : Gesture {
		open class EventData {

		}
	}

	open class Swipe(options: dynamic = definedExternally) : Gesture {
		val escapeVelocity: Number
		val maxProgressStack: Number
		val maxRestTime: Number
		val numInputs: Number
		val timeDistortion: Number

		open class EventData {
			val currentDirection: Double
			val distance: Double
			val duration: Double
			val velocity: Double
		}
	}

	open class GestureEvent<T> : Event {
		val detail : Detail<T>

		open class Detail<T> {
			val data: Array<T>
			val events: Array<dynamic>
		}
	}
}


class ZingTouchExtra {
	data class PanOptions(val numInputs: Number, val threshold: Number)

	data class TapOptions(val minDelay: Int, val maxDelay: Int, val numInputs: Int, val tolerance: Int)

	object GestureStrings {
		const val PAN = "pan"
		const val TAP = "tap"
		const val SWIPE = "swipe"
		const val DISTANCE = "distance"
		const val ROTATE = "rotate"
	}
}