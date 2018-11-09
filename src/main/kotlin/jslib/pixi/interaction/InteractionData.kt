@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("interaction")

package jslib.pixi.interaction

import jslib.pixi.PIXI
import org.w3c.dom.events.Event

open external class InteractionData {
	var global: jslib.pixi.Point
	var identifier: Number
	var originalEvent: Event
	val pointerId: Number
	var target: jslib.pixi.DisplayObject

	fun getLocalPosition(displayObject: jslib.pixi.DisplayObject): jslib.pixi.Point
}