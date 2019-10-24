package engine.graphics.ui.element

import debug.Debug
import debug.DebugLevel
import general.Double2
import definition.jslib.pixi.*
import definition.jslib.pixi.interaction.InteractionEvent
import general.Double2.Companion.set

typealias OnClickListener = ((event: InteractionEvent) -> Unit)

class Button(config: ButtonConfig = ButtonConfig()) : Container() {
	private val textStyle = TextStyle().apply {
		align = "center"
		fill = "white"
	}


	var padding: Double = config.padding
		set(value) {
			field = value
			updateSizeIfAutosize()
		}

	var radius: Double = config.radius

	var isAutosized = config.isAutosized

	var onClickListener: OnClickListener? = config.onClickListener

	var backgroundColor: Int = config.backgroundColor

	var pressedBackgroundColor: Int = config.pressedBackgroundColor

	init {
		width = config.dimensions.x
		height = config.dimensions.y
		buttonMode = true
	}

	private val textView: Text = Text(config.text, textStyle).apply {
		val width = this@Button.width
		val height = this@Button.height

		this.width = width
		this.height = height
		this.scale.set(1, 1)
		x = width / 2
		y = height / 2
	}

	var text: String
		get() = textView.text
		set(value) {
			textView.text = value
		}

	private val backgroundView: definition.jslib.pixi.Graphics = Graphics().apply {
		this.width = this@Button.width
		this.height = this@Button.height
		//buttonMode = true
	}

	init {
		addChild(backgroundView)
		addChild(textView)

		//Needs to be in init
		on("click", this::onClick)
		on("tap", this::onClick)
		on("pointerdown", this::onPointerDown)
		on("pointerup", this::onPointerUp)
		on("pointerupoutside", this::onPointerUp)
		interactive = true

		updateSizeIfAutosize()
		onUpdateBackground(false)
	}

	init {
		pivot.set(config.pivot)
		setPosition(config.position)
	}

	fun setPosition(position: Double2) {
		val pivotedPosition = Double2(position.x - pivot.x * width, position.y - pivot.y * height)
		this.position.set(pivotedPosition)
	}

	private fun updateSizeIfAutosize() {
		if (!isAutosized) return

		val textDimens = measureText(text)
		val width = textDimens.x + padding * 2
		val height = textDimens.y + padding * 2

		this.width = width
		this.height = height

		backgroundView.width = width
		backgroundView.height = height
	}

	private fun measureText(text: String): Double2 {
		val metrics = TextMetrics.measureText(text, textStyle)
		return Double2(metrics.width, metrics.height)
	}

	private fun onPointerDown(event: InteractionEvent) {
		Debug.log(DebugLevel.ALL, event)
		onUpdateBackground(true)
	}

	private fun onPointerUp(event: InteractionEvent) {
		Debug.log(DebugLevel.ALL, event)
		onUpdateBackground(false)
	}

	private fun onClick(event: InteractionEvent) {
		Debug.log(DebugLevel.ALL, event)
		onClickListener?.invoke(event)
	}

	private fun onUpdateBackground(isPressed: Boolean) {
		backgroundView.apply {
			val backgroundColor = if (isPressed) pressedBackgroundColor else backgroundColor

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

	/*override fun destroy() {
		Graphics.uiContainer.removeChild(parent)
	}*/
}

data class ButtonConfig(
	val text: String = "",
	val position: Double2 = Double2(),
	val padding: Double = 0.0,
	val backgroundColor: Int = 0xDE3249,
	val pressedBackgroundColor: Int = 0xaf1d30,
	val isAutosized: Boolean = true,
	val dimensions: Double2 = Double2(),
	val radius: Double = 0.0,
	val pivot: Double2 = Double2(),
	val onClickListener: OnClickListener? = null
)