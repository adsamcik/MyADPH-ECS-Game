@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("UI")

package definition.jslib.pixi.UI

import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.PIXI

abstract external class UIBase : DisplayObject {
	var minWidth: Double
	var maxWidth: Double

	var minHeight: Double
	var maxHeight: Double
}