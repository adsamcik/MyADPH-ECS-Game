@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

open external class Texture {
	companion object {
		fun fromFrame(url: String): Texture
		fun fromImage(imageUrl: String): Texture
		fun fromVideo(videoUrl: String): Texture

		val EMPTY: Texture
		val WHITE: Texture
	}

	var baseTexture: definition.jslib.pixi.BaseTexture
}