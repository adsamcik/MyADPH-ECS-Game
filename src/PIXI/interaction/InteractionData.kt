@file:JsModule("pixi.js")
@file:JsNonModule
@file:JsQualifier("interaction")

package PIXI.interaction

import org.w3c.dom.events.Event

external open class InteractionData {
    var global: PIXI.Point
    var identifier: Number
    var originalEvent: Event
    val pointerId: Number
    var target: PIXI.DisplayObject

    fun getLocalPosition(displayObject: PIXI.DisplayObject): PIXI.Point
}