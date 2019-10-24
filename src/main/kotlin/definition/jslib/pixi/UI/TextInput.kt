@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi.UI

import definition.jslib.pixi.Container
import definition.jslib.pixi.PIXI

external class TextInput(styles: dynamic) : Container {
	var substituteText: String
	var placeholder: String

	var disabled: String
	var text: String
}