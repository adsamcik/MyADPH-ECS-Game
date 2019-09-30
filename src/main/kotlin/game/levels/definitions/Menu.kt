package game.levels.definitions

import engine.graphics.ui.Button
import game.levels.Level
import jslib.pixi.DisplayObject
import jslib.pixi.Graphics
import jslib.pixi.Text
import jslib.pixi.TextStyle
import kotlin.math.PI

class Menu : Level(NAME) {
	private val uiContainer = mutableListOf<DisplayObject>()

	override fun load() {
		val style = TextStyle(
			kotlin.js.json(
				"fontFamily" to "Arial",
				"fontSize" to 36,
				"fontStyle" to "italic",
				"fontWeight" to "bold",
				"fill" to arrayOf("#ffffff", "#00ff99"), // gradient
				"stroke" to "#4a1850",
				"strokeThickness" to 5,
				"dropShadow" to true,
				"dropShadowColor" to "#000000",
				"dropShadowBlur" to 4,
				"dropShadowAngle" to PI / 6,
				"dropShadowDistance" to 6,
				"wordWrap" to true,
				"wordWrapWidth" to 440
			)
		)

		Button(100, 50, "Test")
	}

	private fun addToUi(displayObject: DisplayObject) {
		uiContainer.add(displayObject)
		engine.graphics.Graphics.uiContainer.addChild(displayObject)
	}

	override fun unloadLevel() {
		uiContainer.forEach {
			engine.graphics.Graphics.uiContainer.removeChild(it)
		}
	}

	companion object {
		const val NAME: String = "Menu"
	}
}