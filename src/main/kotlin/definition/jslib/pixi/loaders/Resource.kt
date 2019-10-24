@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("loaders")

package definition.jslib.pixi.loaders

import definition.jslib.pixi.PIXI

external interface Resource {
	val data: dynamic
	val extension: String
	val name: String
	val url: String
}