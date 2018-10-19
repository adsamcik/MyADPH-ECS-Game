@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("ticker")

package jslib.pixi.ticker

external open class Ticker {
	fun add(fn: (Number) -> Unit)
}