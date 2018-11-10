package ecs.eventsystem

import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import engine.entity.Entity
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType
import game.modifiers.ModifierSupervisor

class ModifierEventSystem(physicsEventManager: PhysicsEventManager) :
	PhysicsEventSystem<ModifierSpreaderComponent, ModifierReceiverComponent>(
		physicsEventManager,
		ModifierSpreaderComponent::class,
		ModifierReceiverComponent::class
	) {

	init {
		subscribeOnCollisionStart()
	}

	override fun onTriggered(
		event: PhysicsEventType,
		entityA: Entity,
		componentA: ModifierSpreaderComponent,
		entityB: Entity,
		componentB: ModifierReceiverComponent
	) {
		ModifierSupervisor.addModifier(
			componentB,
			componentA.factory
		)
	}
}