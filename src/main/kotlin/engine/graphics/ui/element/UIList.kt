package engine.graphics.ui.element

import engine.graphics.ui.element.marker.IMeasurable
import jslib.pixi.Container
import jslib.pixi.DisplayObject

class UIList(val orientation: Orientation = Orientation.VERTICAL) : Container(), IMeasurable {
	var listLength: Double = 0.0

	override fun addChild(child: DisplayObject) {
		when (orientation) {
			Orientation.VERTICAL -> {
				child.y = listLength
				listLength += child.height
			}
			Orientation.HORIZONTAL -> {
				child.x = listLength
				listLength += child.width
			}
		}

		super.addChild(child)
	}

	override fun measureHeight(): Double = if (orientation == Orientation.VERTICAL) listLength else height

	override fun measureWidth(): Double = if (orientation == Orientation.HORIZONTAL) listLength else width
}

enum class Orientation {
	HORIZONTAL,
	VERTICAL
}