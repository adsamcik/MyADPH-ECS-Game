@file:JsModule("pixi.js")
@file:JsNonModule
@file:JsQualifier("loaders")

package PIXI.loaders

external open class Loader {
    val resources: Resources

    fun add(url: String): Loader
    fun add(name: String, url: String): Loader
    fun load(fn: (PIXI.loaders.Loader, Resources) -> Unit)
    fun reset()
}
