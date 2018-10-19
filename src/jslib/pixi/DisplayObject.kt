@file:JsModule("pixi")
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
	var pivot: ObservablePoint
	var position: jslib.pixi.Point
	var rotation: Number
	val scale: ObservablePoint
	var width: Number
	var x: Number
	var y: Number

	fun destroy()
	fun on(eventName: String, eventListener: (event: jslib.pixi.interaction.InteractionEvent) -> Unit): DisplayObject
}