@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi.UI

import jslib.pixi.Container
import jslib.pixi.PIXI

external class TextInput(styles: dynamic) : Container {
	var substituteText: String
	var placeholder: String

	var disabled: String
	var string: String

}