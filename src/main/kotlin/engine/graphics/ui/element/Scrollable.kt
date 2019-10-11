package engine.graphics.ui.element

import debug.Debug
import debug.DebugLevel
import engine.types.Rgba
import general.Double2
import jslib.pixi.Container
import jslib.pixi.DisplayObject
import jslib.pixi.Graphics
import jslib.pixi.Rectangle
import kotlin.math.max

class Scrollable : Container() {
	private val scrollbar = Graphics().also { scrollbar ->
		scrollbar.interactive = true
		super.addChild(scrollbar)
	}
	private val internalMask = Graphics()

	init {
		if (Debug.isActive(DebugLevel.WARNING)) {
			super.addChild(internalMask)
		}
	}

	override fun addChild(child: DisplayObject) {
		super.addChild(child)
		setDimensions(width, height)
	}

	fun setDimensions(x: Double, y: Double) {
		this.width = x
		this.height = y

		internalMask.clear().beginFill(Rgba.WHITE.rgb).drawRect(0, 0, x, y).endFill()
		this.mask = internalMask

		var minY = Double.MAX_VALUE
		var maxY = -Double.MAX_VALUE
		children.forEach {
			if (it.y < minY) minY = it.y
			if (it.y > maxY) maxY = it.y + it.getLocalBounds().height
		}

		val visiblePercentage = max(maxY / y, 1.0)

		Debug.log(DebugLevel.ALL, "minY: $minY, maxY: $maxY, percentage: $visiblePercentage")

		scrollbar.position.x = x - SCROLLBAR_WIDTH
		scrollbar
			.clear()
			.beginFill(Rgba.GRAY.rgb)
			.drawRect(0, 0, SCROLLBAR_WIDTH, y * visiblePercentage)
			.endFill()
	}

	fun setDimensions(dimensions: Double2) = setDimensions(dimensions.x, dimensions.y)

	companion object {
		private const val SCROLLBAR_WIDTH = 20
	}
}