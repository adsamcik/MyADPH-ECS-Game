package engine.level

import engine.entity.Entity

class Level(val id: String) {
	private val staticEntities = mutableListOf<Entity>()
	private val dynamicEntities = mutableListOf<Entity>()
	private val playerEntities = mutableListOf<Entity>()

	fun addStaticEntity(entity: Entity) {
		staticEntities.add(entity)
	}

	fun addDynamicEntity(entity: Entity) {
		dynamicEntities.add(entity)
	}

    fun addPlayerEntity(entity: Entity) {
		playerEntities.add(entity)
    }

	fun toJson() = toJson(this)

	companion object {
		fun toJson(level: Level) = JSON.stringify(level)
		fun fromJson(json: String) = JSON.parse<Level>(json)
	}
}