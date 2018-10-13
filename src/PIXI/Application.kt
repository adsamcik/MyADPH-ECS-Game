@file:JsModule("pixi.js")
@file:JsNonModule

package PIXI

import org.w3c.dom.HTMLCanvasElement
import kotlin.js.Json

open external class Application(options: Json = definedExternally) {
    val renderer: Renderer
    val screen: Rectangle
    val stage: Container
    val ticker: PIXI.ticker.Ticker
    val view: HTMLCanvasElement

    constructor(width: Number, height: Number, options: Json = definedExternally)

    fun start()
    fun stop()
}