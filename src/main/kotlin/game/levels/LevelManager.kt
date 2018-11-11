package game.levels

import engine.events.UpdateManager
import engine.interfaces.IUpdatable

object LevelManager : IUpdatable {
	val levels = mutableListOf<Level>()
	var currentLevel = -1


	fun requestNextLevel() {
		UpdateManager.subscribePost(this)
	}

	override fun update(deltaTime: Double) {
		levels[currentLevel].unload()

		if (++currentLevel >= levels.size)
			return

		levels[currentLevel].load()
	}
}