package engine.graphics.ui.element

import debug.Debug
import debug.DebugLevel
import engine.graphics.ui.element.marker.IMeasurable
import engine.types.Rgba
import general.Double2
import jslib.movementY
import jslib.pixi.Container
import jslib.pixi.DisplayObject
import jslib.pixi.Graphics
import jslib.pixi.Rectangle
import jslib.pixi.interaction.InteractionEvent
import org.w3c.dom.events.MouseEvent
import kotlin.math.max

class Scrollable : Container() {
	private var contentHeight: Double = 0.0
	private var childParent = Container().also {
		it.interactive = true
		super.addChild(it)
	}

	private var targetWidth = 0.0
	private var targetHeight = 0.0

	private val scrollbar = Graphics().also { scrollbar ->
		scrollbar.interactive = true
		scrollbar.buttonMode = true
		super.addChild(scrollbar)

		scrollbar.on("pointerdown", this::onScrollbarDragStart)
		scrollbar.on("pointerup", this::onScrollbarDragEnd)
		scrollbar.on("pointerupoutside", this::onScrollbarDragEnd)
	}
	private val internalMask = Graphics()

	init {
		if (Debug.isActive(DebugLevel.WARNING)) {
			super.addChild(internalMask)
		}
	}

	override fun addChild(child: DisplayObject) {
		childParent.addChild(child)
		measure()
	}

	fun setDimensions(x: Double, y: Double) {
		//Setting width and height does not really work and keeps getting overridden or something
		targetWidth = x
		targetHeight = y

		internalMask.clear().beginFill(Rgba.WHITE.rgb).drawRect(0, 0, x, y).endFill()
		this.mask = internalMask

		scrollbar.position.x = x - SCROLLBAR_WIDTH
		measure()
	}

	private fun onScrollbarDragStart(event: InteractionEvent) {
		scrollbar.on("pointermove", this::onScrollbarDragMove)
		event.stopPropagation()
	}

	private fun onScrollbarDragEnd(event: InteractionEvent) {
		scrollbar.off("pointermove")
		event.stopPropagation()
	}

	private fun onScrollbarDragMove(event: InteractionEvent) {
		val offsetY = (event.data.originalEvent as MouseEvent).movementY

		val maxScroll = targetHeight - scrollbar.height
		scrollbar.y = (scrollbar.y + offsetY).coerceIn(0.0, maxScroll)
		val progress = scrollbar.y / maxScroll
		childParent.y = -(progress * (contentHeight - targetHeight)).coerceAtLeast(0.0)
		Debug.log(DebugLevel.ALL, progress, contentHeight, scrollbar.y, maxScroll, childParent.y)

		event.stopPropagation()
	}

	private fun redrawScrollbar(height: Double) {
		//scrollbar.height = height
		scrollbar
			.clear()
			.beginFill(Rgba.GRAY.rgb)
			.drawRect(0, 0, SCROLLBAR_WIDTH, height)
			.endFill()
	}

	fun measure() {
		var sumHeight = 0.0
		childParent.children.forEach {
			sumHeight += if (it is IMeasurable) {
				it.measureHeight()
			} else {
				it.height
			}
		}

		val scrollbarHeight = (targetHeight / sumHeight) * targetHeight
		contentHeight = sumHeight

		redrawScrollbar(scrollbarHeight)
	}

	fun setDimensions(dimensions: Double2) = setDimensions(dimensions.x, dimensions.y)

	companion object {
		private const val SCROLLBAR_WIDTH = 20
	}
}