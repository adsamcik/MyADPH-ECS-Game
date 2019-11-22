package game.levels.definitions

import engine.entity.EntityManager
import game.levels.Level

class CustomLevel(private val definition: String) : Level("custom") {
	override val isGameLevel: Boolean = true

	override fun loadLevel() {
		load(definition)
	}
}