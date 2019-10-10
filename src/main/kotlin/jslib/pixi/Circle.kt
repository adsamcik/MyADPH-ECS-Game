@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi


open external class Circle: IHitArea {
	var x: Number
	var y: Number
	var radius: Number

	val type: Number

	override fun contains(x: Number, y: Number): Boolean
	fun getBounds(): jslib.pixi.Rectangle
	fun clone(): jslib.pixi.Circle
}