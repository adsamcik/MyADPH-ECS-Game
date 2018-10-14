@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("loaders")

package PIXI.loaders

external interface Resource {
	val data: dynamic
	val extension: String
	val name: String
	val url: String
}