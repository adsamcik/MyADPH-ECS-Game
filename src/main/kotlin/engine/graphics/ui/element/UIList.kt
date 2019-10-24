package engine.graphics.ui.element

import engine.graphics.ui.element.marker.IMeasurable
import definition.jslib.pixi.Container
import definition.jslib.pixi.DisplayObject

class UIList(val orientation: Orientation = Orientation.VERTICAL) : Container(), IMeasurable {
	var listLength: Double = 0.0
	private var isDirty = false

	var isRightToLeft: Boolean = false
	var itemPadding: Double = 0.0

	override fun addChild(child: DisplayObject) {
		when (orientation) {
			Orientation.VERTICAL -> {
				child.y = if (isRightToLeft) -listLength - child.height else listLength
				listLength += child.height + itemPadding
			}
			Orientation.HORIZONTAL -> {
				child.x = if (isRightToLeft) -listLength - child.width else listLength
				listLength += child.width + itemPadding
			}
		}

		super.addChild(child)
	}

	fun removeAll() {
		listLength = 0.0
		removeChildren()
	}

	fun setDirty() {
		isDirty = true
	}

	fun update() {
		listLength = if (orientation == Orientation.HORIZONTAL) {
			children.sumByDouble { it.width }
		} else {
			children.sumByDouble { it.height }
		}
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