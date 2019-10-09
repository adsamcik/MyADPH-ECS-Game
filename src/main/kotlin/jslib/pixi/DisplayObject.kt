@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class DisplayObject {
	var alpha: Double
	var buttonMode: Boolean
	var filters: Array<jslib.pixi.Filter>
	var height: Double
	var interactive: Boolean
	var mask: Mask?
	val parent: jslib.pixi.Container
	var pivot: IPoint
	var position: IPoint
	var rotation: Double
	val scale: IPoint
	val skew: ObservablePoint
	var width: Double
	var x: Double
	var y: Double

	var zIndex: Int
	var visible: Boolean

	val worldAlpha: Double
	val worldVisible: Boolean

	fun destroy()
	fun on(eventName: String, eventListener: (event: jslib.pixi.interaction.InteractionEvent) -> Unit): DisplayObject
}