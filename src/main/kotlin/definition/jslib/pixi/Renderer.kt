@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

external interface Renderer {
	val height: Number
	val width: Number

	@Deprecated("autoResize is deprecated since 5.0", replaceWith = ReplaceWith("autoDensity"))
	var autoResize: Boolean

	var autoDensity: Boolean

	val plugins: dynamic

	fun render(
		displayObject: DisplayObject,
		renderTexture: RenderTexture,
		clear: Boolean = definedExternally,
		transform: definition.jslib.pixi.Transform? = definedExternally,
		skipUpdateTransform: Boolean = definedExternally
	)

	fun resize(screenWidth: Int, screenHeight: Int)
}