@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("UI")

package jslib.pixi.UI

import jslib.pixi.PIXI

open external class Button(options: ButtonOptions): InputBase {
	var value: String
	var text: String
}