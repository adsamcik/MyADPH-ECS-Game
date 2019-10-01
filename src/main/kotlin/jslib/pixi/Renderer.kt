@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

external interface Renderer {
	val height: Number
	val width: Number

	@Deprecated("autoResize is deprecated since 5.0", replaceWith = ReplaceWith("autoDensity"))
	var autoResize: Boolean

	var autoDensity: Boolean

	fun render(
		displayObject: DisplayObject,
		renderTexture: RenderTexture,
		clear: Boolean = definedExternally,
		transform: jslib.pixi.Transform? = definedExternally,
		skipUpdateTransform: Boolean = definedExternally
	)

	fun resize(screenWidth: Int, screenHeight: Int)
}