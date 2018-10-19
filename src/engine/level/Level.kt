package engine.level

import engine.entity.Entity

class Level {
	private val staticEntities = mutableListOf<Entity>()
	private val dynamicEntities = mutableListOf<Entity>()
	private val playerEntities = mutableListOf<Entity>()
}