@file:JsModule("pixi.js")
@file:JsNonModule

package PIXI

external open class RenderTexture(baseRenderTexture: BaseRenderTexture) : Texture {
    companion object {
        fun create(width: Number = definedExternally, height: Number = definedExternally, scaleMode: Number = definedExternally, resolution: Number = definedExternally): RenderTexture
    }
}