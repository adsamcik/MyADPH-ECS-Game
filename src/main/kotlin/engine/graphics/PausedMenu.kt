package engine.graphics

import engine.interfaces.IUpdatable
import jslib.pixi.Point
import jslib.pixi.Text
import jslib.pixi.TextStyle
import utility.Rgba
import kotlin.browser.window

object PausedMenu : IUpdatable {
	var pausedText: Text? = null

	fun show() {
		val style = TextStyle()
		style.fill = Rgba.WHITE.rgb
		style.stroke = Rgba.BLACK.rgb
		style.strokeThickness = 2

		val pausedText = Text("PAUSED", style)
		pausedText.anchor.set(0.5, 0.5)
		pausedText.position = Point(window.innerWidth / 2.0, window.innerHeight / 2.0)
		this.pausedText = pausedText

		Graphics.uiContainer.addChild(pausedText)
	}

	fun hide() {
		Graphics.uiContainer.removeChild(pausedText!!)
	}

	override fun update(deltaTime: Double) {

	}

}