package game.levels

import engine.events.UpdateManager
import engine.interfaces.IUpdatable

object LevelManager : IUpdatable {
	private val levels = mutableListOf<Level>()
	var currentLevel = -1
		private set

	fun addLevel(level: Level) {
		levels.add(level)
	}

	fun requestNextLevel() {
		UpdateManager.subscribePost(this)
	}

	override fun update(deltaTime: Double) {
		UpdateManager.unsubscribePost(this)

		if (currentLevel >= 0)
			levels[currentLevel].unload()

		if (++currentLevel >= levels.size)
			return

		levels[currentLevel].load()
	}
}