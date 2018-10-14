@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("ticker")

package PIXI.ticker

external open class Ticker {
	fun add(fn: (Number) -> Unit)
}