@file:JsModule("pixi")
@file:JsNonModule

package jslib.pixi

external open class Texture {
	companion object {
		fun fromFrame(url: String): Texture
		fun fromImage(imageUrl: String): Texture
		fun fromVideo(videoUrl: String): Texture
	}

	var baseTexture: jslib.pixi.BaseTexture
}