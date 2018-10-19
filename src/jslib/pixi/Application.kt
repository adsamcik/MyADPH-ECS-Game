@file:JsModule(("pixi"))
@file:JsNonModule

package jslib.pixi

import org.w3c.dom.HTMLCanvasElement
import kotlin.js.Json

open external class Application(options: Json = definedExternally) {
    val renderer: Renderer
    val screen: Rectangle
    val stage: Container
    val ticker: jslib.pixi.ticker.Ticker
    val view: HTMLCanvasElement

    constructor(width: Number, height: Number, options: dynamic = definedExternally)

    fun start()
    fun stop()
}