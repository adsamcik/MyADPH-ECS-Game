@file:JsModule("pixi.js")
@file:JsNonModule

package PIXI

external open class Point(x: Number = definedExternally, y: Number = definedExternally) {
    var x: Number
    var y: Number

    fun copy(p: PIXI.Point)
    fun set(x: Number = definedExternally, y: Number = definedExternally)
}