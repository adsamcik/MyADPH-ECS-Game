@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("loaders")

package jslib.pixi.loaders

external interface Resource {
	val data: dynamic
	val extension: String
	val name: String
	val url: String
}