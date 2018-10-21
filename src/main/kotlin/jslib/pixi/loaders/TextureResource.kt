// see https://github.com/pixijs/pixi.js/blob/v4.6.2/src/loaders/textureParser.js
package jslib.pixi.loaders

external interface TextureResource : Resource {
	val texture: jslib.pixi.Texture
}
