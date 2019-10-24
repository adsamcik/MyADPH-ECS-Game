@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("extras")

package definition.jslib.pixi.extras

import definition.jslib.pixi.PIXI
import kotlin.js.Json

open external class BitmapText(text: String, style: Json) : definition.jslib.pixi.DisplayObject {
	val textWidth: Number
}