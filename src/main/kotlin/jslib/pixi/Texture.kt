@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Texture {
	companion object {
		fun fromFrame(url: String): Texture
		fun fromImage(imageUrl: String): Texture
		fun fromVideo(videoUrl: String): Texture
	}

	var baseTexture: jslib.pixi.BaseTexture
}