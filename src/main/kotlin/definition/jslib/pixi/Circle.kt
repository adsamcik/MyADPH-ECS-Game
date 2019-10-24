@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi


open external class Circle: IHitArea {
	var x: Number
	var y: Number
	var radius: Number

	val type: Number

	override fun contains(x: Number, y: Number): Boolean
	fun getBounds(): definition.jslib.pixi.Rectangle
	fun clone(): definition.jslib.pixi.Circle
}