@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("interaction")

package definition.jslib.pixi.interaction

import definition.jslib.pixi.PIXI

open external class InteractionEvent {
	var currentTarget: definition.jslib.pixi.DisplayObject
	var data: definition.jslib.pixi.interaction.InteractionData
	var stopped: Boolean
	var target: definition.jslib.pixi.DisplayObject
	var type: String

	fun stopPropagation()
}