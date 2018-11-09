@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Sprite(texture: Texture) : DisplayObject, Mask {
	companion object {
		fun fromImage(imageUrl: String): Sprite
	}

	var texture: Texture
}