@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("loaders")

package definition.jslib.pixi.loaders

import definition.jslib.pixi.PIXI

open external class Loader {
	val resources: Resources

	fun add(url: String): Loader
	fun add(name: String, url: String): Loader
	fun load(fn: (definition.jslib.pixi.loaders.Loader, Resources) -> Unit)
	fun reset()
}
