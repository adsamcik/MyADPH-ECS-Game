package engine.graphics.ui.element

import jslib.pixi.Container
import jslib.pixi.DisplayObject

class UIList : Container() {
	var listHeight: Double = 0.0

	override fun addChild(child: DisplayObject) {
		val bounds = getLocalBounds()
		child.y = bounds.height
		super.addChild(child)

		listHeight += bounds.height

		console.log(child)
	}
}