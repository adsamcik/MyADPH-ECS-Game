@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("UI")

package definition.jslib.pixi.UI

import definition.jslib.pixi.PIXI

open external class Button(options: ButtonOptions): InputBase {
	var value: String
	var text: String
}