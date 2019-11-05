package game.levels.definitions

import game.levels.Level

class EmptyLevel : Level("") {
	override val isGameLevel: Boolean
		get() = false

	override fun loadLevel() = Unit
}