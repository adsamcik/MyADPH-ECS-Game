@file:JsModule("pixi")
@file:JsNonModule

package jslib.pixi


open external class Circle {
	var x: Number
	var y: Number
	var radius: Number

	val type: Number

	fun contains(x: Double, y: Double)
	fun getBounds(): jslib.pixi.Rectangle
	fun clone(): jslib.pixi.Circle
}