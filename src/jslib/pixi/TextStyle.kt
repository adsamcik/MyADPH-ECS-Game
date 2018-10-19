@file:JsModule("PIXI")
@file:JsNonModule

package jslib.pixi

import kotlin.js.Json

open external class TextStyle(style: Json = definedExternally) : DisplayObject {
	var align: String
	var breakWords: Boolean
	var dropShadow: Boolean
	var dropShadowAlpha: Boolean
	var dropShadowAngle: Number
	var dropShadowBlur: Number
	var dropShadowColor: dynamic
	var dropShadowDistance: Number

	var fill: dynamic
	var fillGradientStops: Array<Double>
	var fillGradientType: Int
	var fontFamily: dynamic
	var fontSize: dynamic
	var fontStyle: String
	var fontVariant: String
	var fontWeight: String
	var leading: Number
	var letterSpacing: Number
	var lineHeight: Number
	var lineJoin: String
	var milterLimit: Number
	var padding: Number
	var stroke: dynamic
	var strokeThickness: Number
	var textBaseline: String
	var trim: Boolean
	var whiteSpace: String
	var wordWrap: Boolean
	var wordWrapWidth: Number

	fun toFontString(): String
	fun reset()
	fun clone(): jslib.pixi.TextStyle
}