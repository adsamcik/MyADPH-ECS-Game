@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Sprite(texture: Texture) : DisplayObject, Mask {
	companion object {
		fun fromImage(imageUrl: String): Sprite
	}

	var texture: Texture
	var anchor: ObservablePoint
	//enum
	var blendMode: Number
	var tint: Number
	var pluginName: String
}