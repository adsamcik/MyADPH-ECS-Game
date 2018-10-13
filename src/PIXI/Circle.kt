@file:JsModule("pixi.js")
@file:JsNonModule

package PIXI


open external class Circle {
	var x: Number
	var y: Number
	var radius: Number

	fun contains(x: Double, y: Double)
}