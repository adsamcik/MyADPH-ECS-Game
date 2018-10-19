@file:JsModule("pixi")
@file:JsNonModule

package jslib.pixi

open external class Graphics(nativeLines: Boolean = definedExternally) : DisplayObject, Mask {

	fun beginFill(color: Number = definedExternally, alpha: Number = definedExternally): Graphics
	fun bezierCurveTo(cpX: Double, cpY: Double, cpX2: Double, cpY2: Double, toX: Double, toY: Double): Graphics
	fun clear(): Graphics
	fun drawCircle(x: Number, y: Number, radius: Number): Graphics
	fun drawRect(x: Number, y: Number, width: Number, height: Number): Graphics
	fun drawRoundedRect(x: Number, y: Number, width: Number, height: Number, radius: Number): Graphics
	fun endFill(): Graphics
	fun lineStyle(lineWidth: Number = definedExternally, color: Number = definedExternally, alpha: Number = definedExternally): Graphics
	fun lineTo(x: Number, y: Number): Graphics
	fun moveTo(x: Number, y: Number): Graphics
	fun quadraticCurveTo(cpX: Number, cpY: Number, toX: Number, toY: Number): Graphics
}
