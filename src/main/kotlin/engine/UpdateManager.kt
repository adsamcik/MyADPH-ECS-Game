package engine

import engine.interfaces.IUpdatable

object UpdateManager {
	private val updateList = mutableListOf<IUpdatable>()

	fun subscribe(updatable: IUpdatable) {
		updateList.add(updatable)
	}

	fun unsubscribe(updatable: IUpdatable) {
		updateList.remove(updatable)
	}

	fun update(deltaTime: Double) {
		updateList.forEach { it.update(deltaTime) }
	}
}