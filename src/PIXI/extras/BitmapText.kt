@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("extras")

package PIXI.extras

import kotlin.js.Json

external open class BitmapText(text: String, style: Json) : PIXI.DisplayObject {
	val textWidth: Number
}