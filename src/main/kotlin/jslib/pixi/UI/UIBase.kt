@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("UI")

package jslib.pixi.UI

import jslib.pixi.DisplayObject
import jslib.pixi.PIXI

abstract external class UIBase : DisplayObject {
	var minWidth: Double
	var maxWidth: Double

	var minHeight: Double
	var maxHeight: Double
}