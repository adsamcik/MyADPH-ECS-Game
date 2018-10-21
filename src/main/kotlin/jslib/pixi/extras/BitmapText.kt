@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("extras")

package jslib.pixi.extras

import kotlin.js.Json

external open class BitmapText(text: String, style: Json) : jslib.pixi.DisplayObject {
	val textWidth: Number
}