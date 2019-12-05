package game.levels.definitions

import definition.jslib.Swal
import definition.jslib.SwalData
import engine.graphics.Graphics
import engine.graphics.ui.element.Button
import engine.graphics.ui.element.ButtonConfig
import game.levels.Level
import game.levels.LevelManager
import general.Double2
import definition.jslib.pixi.*
import definition.jslib.pixi.interaction.InteractionEvent
import engine.graphics.ui.*
import extensions.createDiv
import extensions.isValidJson
import extensions.requireParentElement
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

class Menu : Level(NAME) {
	override val isGameLevel: Boolean = false

	private var rootElement: Element? = null

	private fun loadCustomLevel() {
		Swal.fire(
			SwalData(
				title = "Insert level definition",
				showCancelButton = true,
				input = "text",
				inputValidator = {
					try {
						when {
							it.isBlank() -> "You need to enter level definition"
							!it.isValidJson -> "Definition you entered is not a valid JSON format"
							else -> {
								LevelManager.loadCustomLevel(it)
								null
							}
						}
					} catch (e: Exception) {
						e.message
					}
				})
		)
	}

	override fun loadLevel() {
		rootElement = document.createDiv(parent = document.body) { root ->
			root.addClass("fullsized", "html-ui")
			root.addList(MenuOrientation.VERTICAL) { list ->
				list.addClass("centered")
				list.addListItem { listItem ->
					listItem.addUIBigButton("Campaign", { LevelManager.requestLevel("Level1") })
				}

				list.addListItem { listItem ->
					listItem.addUIBigButton("Play custom map", { loadCustomLevel() })
				}

				list.addListItem { listItem ->
					listItem.addUIBigButton("Editor", { LevelManager.requestLevel("Editor") })
				}

			}
		}
	}

	override fun unloadLevel() {
		rootElement?.let {
			it.requireParentElement().removeChild(it)
		}
	}

	companion object {
		const val NAME: String = "Menu"
		private const val BUTTON_HEIGHT = 50
		private const val BUTTON_WIDTH = 100
		private const val SPACE_BETWEEN_BUTTONS = 25
	}
}