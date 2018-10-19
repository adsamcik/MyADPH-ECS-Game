@file:JsModule("PIXI")
@file:JsNonModule

package jslib.pixi

external interface Renderer {
	fun render(
			displayObject: DisplayObject,
			renderTexture: RenderTexture,
			clear: Boolean = definedExternally,
			transform: jslib.pixi.Transform? = definedExternally,
			skipUpdateTransform: Boolean = definedExternally
	)
}