package engine.graphics.ui

import debug.Debug
import debug.DebugLevel
import engine.graphics.Graphics
import general.Double2
import jslib.pixi.*
import jslib.pixi.interaction.InteractionEvent
import kotlin.math.roundToInt


class Button(
	position: Point,
	val title: String,
	private val radius: Double = 0.0
) {
	var position: Point
		get() {
			val position = parent.position
			return Point(position.x, position.y)
		}
		set(value) {
			parent.position.set(value.x, value.y)
		}

	private val textStyle = TextStyle().apply {
		align = "center"
		fill = "white"
	}

	var width: Int = 0
		set(value) {
			field = value
			autoSize = false
		}

	var height: Int = 0
		set(value) {
			field = value
			autoSize = false
		}


	var padding: Int = 16
		set(value) {
			field = value
			updateSizeIfAutosize()
		}

	var autoSize = true

	init {
		updateSizeIfAutosize()
	}

	var onClickListener: ((event: InteractionEvent) -> Unit)? = null

	private val text: Text = Text(title, textStyle).apply {
		val width = this@Button.width
		val height = this@Button.height

		this.width = width
		this.height = height
		anchor.set(0.5, 0.5)
		this.scale.set(1, 1)
		x = width / 2
		y = height / 2
	}

	private fun updateSizeIfAutosize() {
		if (!autoSize) return

		val textDimens = measureText(title)
		width = textDimens.x.roundToInt() + padding * 2
		height = textDimens.y.roundToInt() + padding * 2
	}

	private fun measureText(text: String): Double2 {
		val metrics = TextMetrics.measureText(text, textStyle)
		return Double2(metrics.width, metrics.height)
	}

	private val background: jslib.pixi.Graphics = Graphics().apply {
		this.width = this@Button.width
		this.height = this@Button.height
	}

	private val parent: Container = Container().apply {
		pivot = Point(0.5, 0.5)
		this.x = position.x
		this.y = position.y

		this.width = this@Button.width
		this.height = this@Button.height
		interactive = true
	}.also { container ->
		container.addChild(background)
		container.addChild(text)
		Graphics.uiContainer.addChild(container)
	}

	init {
		//Needs to be in init
		parent.on("click", this::onClick)
		parent.on("tap", this::onClick)
		parent.on("pointerdown", this::onPointerDown)
		parent.on("pointerup", this::onPointerUp)
		parent.on("pointerupoutside", this::onPointerUp)

		onUpdateBackground(false)
	}

	private fun onPointerDown(event: InteractionEvent) {
		Debug.log(DebugLevel.ALL, event)
		onUpdateBackground(true)
	}

	private fun onPointerUp(event: InteractionEvent) {
		Debug.log(DebugLevel.ALL, event)
		onUpdateBackground(false)
	}

	private fun onUpdateBackground(isPressed: Boolean) {
		background.apply {
			val backgroundColor = if (isPressed) PRESSED_BACKGROUND else BACKGROUND

			clear()
			beginFill(backgroundColor)
			if (radius == 0.0) {
				drawRect(0, 0, this@Button.width, this@Button.height)
			} else {
				//looks really bad at the time of implementation
				drawRoundedRect(0, 0, this@Button.width, this@Button.height, radius)
			}
			endFill()
		}
	}

	private fun onClick(event: InteractionEvent) {
		Debug.log(DebugLevel.ALL, event)
		onClickListener?.invoke(event)
	}

	fun destroy() {
		Graphics.uiContainer.removeChild(parent)
	}

	companion object {
		private const val PRESSED_BACKGROUND = 0xaf1d30
		private const val BACKGROUND = 0xDE3249
	}
}