@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Point(x: Number = definedExternally, y: Number = definedExternally) {
	var x: Number
	var y: Number

	fun copy(p: jslib.pixi.Point)
	fun set(x: Number = definedExternally, y: Number = definedExternally)
}