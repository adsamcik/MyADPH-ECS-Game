package game.levels

abstract class CollectionLevel(id: String) : Level(id) {
	override val isGameLevel: Boolean = true

	abstract val nextLevelId: String?
}