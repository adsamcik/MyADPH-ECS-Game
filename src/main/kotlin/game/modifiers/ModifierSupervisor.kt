package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.events.UpdateManager
import engine.events.IUpdatable

object ModifierSupervisor : IUpdatable {
	private var updatesEnabled = false

	private fun enableUpdates() {
		UpdateManager.subscribePost(this)
		updatesEnabled = true
	}

	private fun disableUpdates() {
		UpdateManager.unsubscribePost(this)
		updatesEnabled = false
	}


	private val pendingModifiers = mutableListOf<Pair<ModifierReceiverComponent, ModifierCommandFactory>>()

	override fun update(deltaTime: Double) {
		if (pendingModifiers.isEmpty())
			disableUpdates()
		else {
			pendingModifiers.forEach { (receiver, factory) ->
				factory.apply(receiver)
			}

			pendingModifiers.clear()
		}
	}


	fun addModifier(receiver: ModifierReceiverComponent, factory: ModifierCommandFactory) {
		pendingModifiers.add(Pair(receiver, factory))
		if (!updatesEnabled)
			enableUpdates()
	}
}