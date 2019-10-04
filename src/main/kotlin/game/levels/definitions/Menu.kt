package game.levels.definitions

import engine.graphics.Graphics
import game.levels.Level
import game.levels.LevelManager
import jslib.pixi.DisplayObject
import jslib.pixi.Point
import jslib.pixi.TextStyle
import jslib.pixi.Texture
import jslib.pixi.UI.Button
import jslib.pixi.UI.ButtonOptions
import jslib.pixi.UI.Sprite
import jslib.pixi.interaction.InteractionEvent
import kotlinx.serialization.json.json
import kotlin.math.PI

class Menu : Level(NAME) {
	override val isGameLevel: Boolean = false

	private val uiContainer = mutableListOf<DisplayObject>()

	private val buttonList = listOf(
		addButton("Campaign") { LevelManager.requestLevel("Level1") },
		addButton("Play custom map") {},
		addButton("Editor") { LevelManager.requestLevel("Editor") }
	)

	override fun loadLevel() {
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

		val totalHeight = buttonList.sumBy { it.height.toInt() } + (buttonList.size - 1) * SPACE_BETWEEN_BUTTONS
		val centerX = Graphics.pixi.screen.width / 2
		val centerY = Graphics.pixi.screen.height / 2

		val startAtY = centerY - totalHeight / 2
		buttonList.forEachIndexed { index, button ->
			button.position = Point(centerX, startAtY + index * BUTTON_HEIGHT + index * SPACE_BETWEEN_BUTTONS)
		}

		console.log(buttonList)
	}

	private fun addButton(title: String, clickListener: (event: InteractionEvent) -> Unit): Button {
		val options = ButtonOptions(background = Sprite(Texture.WHITE))
		console.log(options)
		return Button(options).also {
			it.on("click", clickListener)
			it.text = title
		}
	}

	private fun addToUi(displayObject: DisplayObject) {
		uiContainer.add(displayObject)
		Graphics.uiContainer.addChild(displayObject)
	}

	override fun unloadLevel() {
		uiContainer.forEach {
			Graphics.uiContainer.removeChild(it)
		}

		buttonList.forEach {
			it.destroy()
		}
	}

	companion object {
		const val NAME: String = "Menu"
		private const val BUTTON_HEIGHT = 50
		private const val BUTTON_WIDTH = 100
		private const val SPACE_BETWEEN_BUTTONS = 25
	}
}