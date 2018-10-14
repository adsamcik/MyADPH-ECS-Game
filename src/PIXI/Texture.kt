@file:JsModule("PIXI")
@file:JsNonModule

package PIXI

external open class Texture {
	companion object {
		fun fromFrame(url: String): Texture
		fun fromImage(imageUrl: String): Texture
		fun fromVideo(videoUrl: String): Texture
	}

	var baseTexture: PIXI.BaseTexture
}