@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("UI")

package jslib.pixi.UI

import jslib.pixi.PIXI
import jslib.pixi.Texture

open external class Sprite(texture: Texture) : UIBase {

	companion object {
		fun fromImage(imageUrl: String): Sprite
		fun fromFrame(frameId: Any): Sprite
	}
}