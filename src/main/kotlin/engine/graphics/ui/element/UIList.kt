package engine.graphics.ui.element

import engine.graphics.ui.element.marker.IMeasurable
import jslib.pixi.Container
import jslib.pixi.DisplayObject

class UIList : Container(), IMeasurable {
	var listHeight: Double = 0.0

	override fun addChild(child: DisplayObject) {
		val height = child.height
		child.y = listHeight
		super.addChild(child)

		listHeight += height

		console.log(child)
	}

	override fun measureHeight(): Double = listHeight

	override fun measureWidth(): Double = width
}