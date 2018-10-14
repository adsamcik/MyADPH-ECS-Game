@file:JsModule("PIXI")
@file:JsNonModule

package PIXI

open external class Point(x: Number = definedExternally, y: Number = definedExternally) {
	var x: Number
	var y: Number

	fun copy(p: PIXI.Point)
	fun set(x: Number = definedExternally, y: Number = definedExternally)
}