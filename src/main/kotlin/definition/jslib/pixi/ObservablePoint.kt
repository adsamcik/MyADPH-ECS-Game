@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

open external class ObservablePoint(
	callback: () -> Unit,
	scope: Any,
	x: Number = definedExternally,
	y: Number = definedExternally
) : IPoint {
	override var x: Double
	override var y: Double

	fun clone(): ObservablePoint

	override fun copyFrom(observablePoint: IPoint)
	override fun copyTo(observablePoint: IPoint)

	override fun set(x: Number, y: Number)

	@Suppress("CovariantEquals")
	override fun equals(point: IPoint): Boolean
}