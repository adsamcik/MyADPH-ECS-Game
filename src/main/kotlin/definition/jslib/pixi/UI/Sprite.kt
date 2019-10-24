@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("UI")

package definition.jslib.pixi.UI

import definition.jslib.pixi.PIXI
import definition.jslib.pixi.Texture

open external class Sprite(texture: Texture) : UIBase {

	companion object {
		fun fromImage(imageUrl: String): Sprite
		fun fromFrame(frameId: Any): Sprite
	}
}