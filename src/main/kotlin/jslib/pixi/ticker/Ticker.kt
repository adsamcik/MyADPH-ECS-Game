@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("ticker")

package jslib.pixi.ticker

import jslib.pixi.PIXI

open external class Ticker {
	fun add(fn: (Number) -> Unit)
}