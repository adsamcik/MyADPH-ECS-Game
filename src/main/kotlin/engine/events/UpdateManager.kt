package engine.events

object UpdateManager {
	private val updateList = mutableListOf<IUpdatable>()

	private val postUpdateList = mutableListOf<IUpdatable>()

	private val preUpdateList = mutableListOf<IUpdatable>()

	fun subscribe(updatable: IUpdatable) {
		updateList.add(updatable)
	}

	fun subscribePost(postUpdate: IUpdatable) {
		postUpdateList.add(postUpdate)
	}

	fun subscribePre(preUpdate: IUpdatable) {
		preUpdateList.add(preUpdate)
	}

	fun unsubscribe(updatable: IUpdatable) {
		updateList.remove(updatable)
	}

	fun unsubscribePost(updatable: IUpdatable) {
		postUpdateList.remove(updatable)
	}

	fun unsubscribePre(updatable: IUpdatable) {
		preUpdateList.remove(updatable)
	}

	fun update(deltaTime: Double) {
		preUpdateList.forEach { it.update(deltaTime) }
		updateList.forEach { it.update(deltaTime) }
		postUpdateList.forEach { it.update(deltaTime) }
	}
}