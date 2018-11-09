@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("extras")

package jslib.pixi.extras

import jslib.pixi.PIXI
import kotlin.js.Json

open external class BitmapText(text: String, style: Json) : jslib.pixi.DisplayObject {
	val textWidth: Number
}