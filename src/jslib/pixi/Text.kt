@file:JsModule("PIXI")
@file:JsNonModule

package jslib.pixi

import kotlin.js.Json

external open class Text(text: String, style: TextStyle = definedExternally) : DisplayObject {
	constructor(text: String, style: Json)

	var text: String
}