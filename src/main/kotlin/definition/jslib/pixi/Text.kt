@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

import kotlin.js.Json

open external class Text(text: String, style: TextStyle = definedExternally) : Sprite {
	constructor(text: String, style: Json)

	var text: String
	var style: TextStyle
}