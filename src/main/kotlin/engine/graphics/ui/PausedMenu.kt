package engine.graphics.ui

import engine.events.IUpdatable
import engine.graphics.Graphics
import definition.jslib.pixi.Point
import definition.jslib.pixi.Text
import definition.jslib.pixi.TextStyle
import engine.types.Rgba
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
		PausedMenu.pausedText = pausedText

		Graphics.staticUIContainer.addChild(pausedText)
	}

	fun hide() {
		Graphics.staticUIContainer.removeChild(pausedText!!)
	}

	override fun update(deltaTime: Double) {

	}

}