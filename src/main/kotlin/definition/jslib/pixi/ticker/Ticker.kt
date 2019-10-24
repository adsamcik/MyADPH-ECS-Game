@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("ticker")

package definition.jslib.pixi.ticker

import definition.jslib.pixi.PIXI

open external class Ticker {
	fun add(fn: (Number) -> Unit)
}