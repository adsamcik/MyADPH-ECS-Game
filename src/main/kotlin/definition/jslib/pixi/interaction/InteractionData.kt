@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("interaction")

package definition.jslib.pixi.interaction

import definition.jslib.pixi.PIXI
import org.w3c.dom.events.Event

open external class InteractionData {
	var global: definition.jslib.pixi.Point
	var identifier: Number
	var originalEvent: Event
	val pointerId: Number
	var target: definition.jslib.pixi.DisplayObject

	fun getLocalPosition(displayObject: definition.jslib.pixi.DisplayObject): definition.jslib.pixi.Point
}