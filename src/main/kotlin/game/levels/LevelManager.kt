package game.levels

import debug.Debug
import debug.DebugLevel
import engine.events.UpdateManager
import engine.events.IUpdatable
import game.levels.definitions.CustomLevel
import game.levels.definitions.EmptyLevel

object LevelManager : IUpdatable {
	private val levels = mutableListOf<Level>()

	var nextLevel: Level? = null
		private set

	private var loadedLevel: Level? = null

	private val onLevelChange = mutableListOf<ILevelLoadListener>()

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
		nextLevel = levels[level - 1]
		requestLevelChange()
	}

	fun requestLevel(levelName: String) {
		val level = levels.find { it.id == levelName }
		require(level != null) { "Could not find level $levelName. Registered levels are: ${levels.joinToString { it.id }}" }
		nextLevel = level
		requestLevelChange()
	}

	fun loadCustomLevel(definition: String) {
		val customLevel = CustomLevel(definition)
		nextLevel = customLevel
		requestLevelChange()
	}

	override fun update(deltaTime: Double) {
		UpdateManager.unsubscribePost(this)

		loadedLevel?.let { level ->
			level.unload()
			onLevelChange.forEach { it.onAfterLevelUnload() }
		}

		val nextLevel = this.nextLevel
		if (nextLevel == null) {
			Debug.log(DebugLevel.CRITICAL, "Trying to load null level.")
			return
		}

		nextLevel.load()
		loadedLevel = nextLevel
		this.nextLevel = null
	}


	fun subscribeLoad(loadListener: ILevelLoadListener) {
		onLevelChange.add(loadListener)
	}

	fun unsubscribeLoad(loadListener: ILevelLoadListener) {
		onLevelChange.remove(loadListener)
	}
}