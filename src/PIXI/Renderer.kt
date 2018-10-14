@file:JsModule("PIXI")
@file:JsNonModule

package PIXI

external interface Renderer {
	fun render(
			displayObject: DisplayObject,
			renderTexture: RenderTexture,
			clear: Boolean = definedExternally,
			transform: PIXI.Transform? = definedExternally,
			skipUpdateTransform: Boolean = definedExternally
	)
}