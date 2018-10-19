@file:JsModule("pixi")
@file:JsNonModule

package jslib.pixi

external open class Sprite(texture: Texture) : DisplayObject, Mask {
	companion object {
		fun fromImage(imageUrl: String): Sprite
	}

	var texture: Texture
}