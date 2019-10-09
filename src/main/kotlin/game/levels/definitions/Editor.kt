package game.levels.definitions

import engine.graphics.Graphics
import engine.graphics.ui.Button
import engine.graphics.ui.ButtonConfig
import game.levels.Level
import general.Double2

class Editor : Level("Editor") {
	override val isGameLevel: Boolean = false

	override fun loadLevel() {
		Graphics.levelUIContainer.addChild(
			Button(
				ButtonConfig(
					text = "New entity",
					position = Double2(x = Graphics.dimensions.x, y = 0.0),
					pivot = Double2(1.0, 0.0)
				)
			)
		)
	}
}