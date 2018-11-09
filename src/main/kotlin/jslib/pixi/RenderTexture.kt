@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class RenderTexture(baseRenderTexture: BaseRenderTexture) : Texture {
	companion object {
		fun create(width: Number = definedExternally, height: Number = definedExternally, scaleMode: Number = definedExternally, resolution: Number = definedExternally): RenderTexture
	}
}