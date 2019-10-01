package game.levels

import engine.events.UpdateManager
import engine.interfaces.IUpdatable

object LevelManager : IUpdatable {
	private val levels = mutableListOf<Level>()
	var nextLevel = 0
		private set

	private var loadedLevel: Level? = null

	fun addLevel(level: Level) {
		levels.add(level)
	}

	fun requestLevelChange() {
		UpdateManager.subscribePost(this)
	}

	/**
	 * Request level by number.
	 * First level is 1, 2, 3 and so on (so it's basically index + 1).
	 * This way it is clearer for level numbering.
	 */
	fun requestLevel(level: Int) {
		nextLevel = level - 1
		requestLevelChange()
	}

	fun requestLevel(levelName: String) {
		val index = levels.indexOfFirst { it.id == levelName }
		require(index >= 0) { "Could not find level $levelName. Registered levels are: ${levels.joinToString { it.id }}" }
		nextLevel = index
		requestLevelChange()
	}

	override fun update(deltaTime: Double) {
		UpdateManager.unsubscribePost(this)

		loadedLevel?.unload()

		if (nextLevel >= levels.size)
			return

		val levelToLoad = levels[nextLevel]
		levelToLoad.load()
		loadedLevel = levelToLoad

		nextLevel++
	}
}