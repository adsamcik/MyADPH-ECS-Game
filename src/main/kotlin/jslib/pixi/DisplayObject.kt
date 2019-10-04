@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class DisplayObject {
	var alpha: Number
	var buttonMode: Boolean
	var filters: Array<jslib.pixi.Filter>
	var height: Number
	var interactive: Boolean
	var mask: Mask?
	val parent: jslib.pixi.Container
	var pivot: IPoint
	var position: IPoint
	var rotation: Number
	val scale: IPoint
	val skew: ObservablePoint
	var width: Number
	var x: Number
	var y: Number

	var zIndex: Int
	var visible: Boolean

	val worldAlpha: Double
	val worldVisible: Boolean

	fun destroy()
	fun on(eventName: String, eventListener: (event: jslib.pixi.interaction.InteractionEvent) -> Unit): DisplayObject
}