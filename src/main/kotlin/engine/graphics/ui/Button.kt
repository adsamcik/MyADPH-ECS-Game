package engine.graphics.ui

import engine.graphics.Graphics
import general.Double2
import jslib.pixi.*


class Button(val width: Int, val height: Int, val title: String) {

	private val text: Text = Text(title, TextStyle().apply {
		align = "center"
		fill = "white"
	}).apply {
		this.width = this@Button.width
		this.height = this@Button.height
	}

	private val background: jslib.pixi.Graphics = jslib.pixi.Graphics().apply {
		beginFill(0xDE3249)
		drawRect(0, 0, this@Button.width, this@Button.height)
		endFill()
	}

	private val parent: Container = Container().apply {
		pivot = Point(0.5, 0.5)
	}

	init {
		parent.addChild(background)
		parent.addChild(text)
		Graphics.uiContainer.addChild(parent)

		parent.x = Graphics.pixi.screen.width / 2
		parent.y = Graphics.pixi.screen.height / 2
	}


	private fun update() {

	}

	fun destroy() {
		engine.graphics.Graphics.let {
			it.uiContainer.removeChild(parent)
		}
	}
}