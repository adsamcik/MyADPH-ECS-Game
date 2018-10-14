@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("interaction")

package PIXI.interaction

external open class InteractionEvent {
	var currentTarget: PIXI.DisplayObject
	var data: PIXI.interaction.InteractionData
	var stopped: Boolean
	var target: PIXI.DisplayObject
	var type: String

	fun stopPropagation()
}