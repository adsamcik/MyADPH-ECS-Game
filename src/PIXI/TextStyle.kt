@file:JsModule("PIXI")
@file:JsNonModule

package PIXI

import kotlin.js.Json

open external class TextStyle(style: Json = definedExternally) : DisplayObject {
	var align: String
	var breakWords: Boolean
	var dropShadow: Boolean
	var dropShadowAlpha: Boolean
	var dropShadowAngle: Double
	var dropShadowBlur: Double
	var dropShadowColor: dynamic
	var dropShadowDistance: Double

	var fill: dynamic
	var fillGradientStops: Array<Double>
	var fillGradientType: Int
	var fontFamily: dynamic
	var fontSize: dynamic
	var fontStyle: String
	var fontVariant: String
	var fontWeight: String
	var leading: Double
	var letterSpacing: Double
	var lineHeight: Double
	var lineJoin: String
	var milterLimit: Double
	var padding: Double
	var stroke: dynamic
	var strokeThickness: Double
	var textBaseline: String
	var trim: Boolean
	var whiteSpace: String
	var wordWrap: Boolean
	var wordWrapWidth: Double

	fun toFontString(): String
	fun reset()
	fun clone(): PIXI.TextStyle
}