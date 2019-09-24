@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Rectangle(x: Number, y: Number, width: Number, height: Number) {
	var x: Double
	var y: Double
	var width: Double
	var height: Double

	val bottom: Double
	val left: Double
	val right: Double
	val top: Double
	val type: Number

	companion object {
		val EMPTY: Rectangle
	}
}