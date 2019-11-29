package game.levels.definitions

import engine.graphics.Graphics
import engine.graphics.ui.element.Button
import engine.graphics.ui.element.ButtonConfig
import game.levels.Level
import game.levels.LevelManager
import general.Double2
import definition.jslib.pixi.*
import definition.jslib.pixi.interaction.InteractionEvent
import kotlin.browser.window

class Menu : Level(NAME) {
	override val isGameLevel: Boolean = false

	private var buttonList = emptyList<Button>()

	private fun loadCustomLevel() {
		val result = window.prompt("Insert level definition")
		if (!result.isNullOrBlank()) {
			LevelManager.loadCustomLevel(result)
		}
	}

	override fun loadLevel() {
		buttonList = listOf(
			addButton("Campaign") { LevelManager.requestLevel("Level1") },
			addButton("Play custom map") { loadCustomLevel() },
			addButton("Editor") { LevelManager.requestLevel("Editor") }
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
		//val options = ButtonOptions(background = Sprite(Texture.WHITE))
		return Button(
			ButtonConfig(
				text = title,
				pivot = Double2(0.5, 0.5)
			)
		).apply {
			onClickListener = clickListener
			//on("click", clickListener)
			//text = title
			addToUi(this)
		}/*.also { button ->
			addToUi(button)
		}*/
	}

	private fun addToUi(displayObject: DisplayObject) {
		Graphics.levelUIContainer.addChild(displayObject)
	}

	override fun unloadLevel() {
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