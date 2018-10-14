@file:JsModule("PIXI")
@file:JsNonModule

package PIXI


open external class Circle {
	var x: Number
	var y: Number
	var radius: Number

	val type: Number

	fun contains(x: Double, y: Double)
	fun getBounds(): PIXI.Rectangle
	fun clone(): PIXI.Circle
}