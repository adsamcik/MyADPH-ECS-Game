@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

open external class Point(x: Number = definedExternally, y: Number = definedExternally): IPoint {
	override var x: Double
	override var y: Double

	fun clone(): Point

	override fun copyFrom(observablePoint: IPoint)
	override fun copyTo(observablePoint: IPoint)

	override fun set(x: Number, y: Number)

	@Suppress("CovariantEquals")
	override fun equals(point: IPoint): Boolean
}