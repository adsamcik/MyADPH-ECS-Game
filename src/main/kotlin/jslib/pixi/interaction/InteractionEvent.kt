@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("interaction")

package jslib.pixi.interaction

external open class InteractionEvent {
	var currentTarget: jslib.pixi.DisplayObject
	var data: jslib.pixi.interaction.InteractionData
	var stopped: Boolean
	var target: jslib.pixi.DisplayObject
	var type: String

	fun stopPropagation()
}