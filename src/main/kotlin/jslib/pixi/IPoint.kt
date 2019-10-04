@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

external interface IPoint {
	var x: Double
	var y: Double

	fun copyFrom(observablePoint: IPoint)
	fun copyTo(observablePoint: IPoint)

	fun set(x: Number = definedExternally, y: Number = definedExternally)

	@Suppress("CovariantEquals")
	fun equals(point: IPoint): Boolean
}