@file:JsModule("PIXI")
@file:JsNonModule

package PIXI

external open class Sprite(texture: Texture) : DisplayObject, Mask {
	companion object {
		fun fromImage(imageUrl: String): Sprite
	}

	var texture: Texture
}