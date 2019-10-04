@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

import org.w3c.dom.HTMLCanvasElement

open external class TextMetrics(
	text: String,
	style: TextStyle,
	width: Number = definedExternally,
	height: Number = definedExternally,
	lines: Array<String> = definedExternally,
	lineWidths: Array<Number> = definedExternally,
	lineHeight: Number = definedExternally,
	maxLineWidth: Number = definedExternally,
	fontProperties: Any = definedExternally
) {
	var width: Double
	var height: Double
	var lines: Array<String>
	var lineWidths: Array<Double>
	var lineHeight: Double
	var maxLineWidth: Double
	var style: TextStyle
	var fontProperties: IFontMetrics
	var text: String

	companion object {
		fun clearMetrics(font: String = definedExternally)
		fun measureFont(font: String): IFontMetrics
		fun measureText(text: String, style: TextStyle, wordWrap: Boolean = definedExternally, canvas: HTMLCanvasElement = definedExternally): TextMetrics
	}
}