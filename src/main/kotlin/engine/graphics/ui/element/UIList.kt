package engine.graphics.ui.element

import engine.graphics.ui.element.marker.IMeasurable
import jslib.pixi.Container
import jslib.pixi.DisplayObject

class UIList(val orientation: Orientation = Orientation.VERTICAL) : Container(), IMeasurable {
	var listLength: Double = 0.0
	private var isDirty = false

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

	fun setDirty() {
		isDirty = true
	}

	override fun measureHeight(): Double {
		return if (orientation == Orientation.VERTICAL) {
			if (!isDirty) {
				listLength
			} else {
				children.sumByDouble { it.height }
			}
		} else {
			height
		}
	}

	override fun measureWidth(): Double {
		return if (orientation == Orientation.HORIZONTAL) {
			if (!isDirty) {
				listLength
			} else {
				children.sumByDouble { it.width }
			}
		} else {
			width
		}
	}
}

enum class Orientation {
	HORIZONTAL,
	VERTICAL
}