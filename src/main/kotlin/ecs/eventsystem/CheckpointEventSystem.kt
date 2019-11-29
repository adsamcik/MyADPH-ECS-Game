package ecs.eventsystem

import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointMemoryComponent
import engine.entity.Entity
import engine.physics.bodies.BodyEdit
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType
import game.levels.LevelManager
import engine.types.Rgba

class CheckpointEventSystem(physicsEventManager: PhysicsEventManager) :
	PhysicsEventSystem<CheckpointComponent, CheckpointMemoryComponent>(
		physicsEventManager,
		CheckpointComponent::class,
		CheckpointMemoryComponent::class
	) {

	init {
		subscribeOnCollisionStart()
	}

	override fun onTriggered(
		event: PhysicsEventType,
		entityA: Entity,
		componentA: CheckpointComponent,
		entityB: Entity,
		componentB: CheckpointMemoryComponent
	) {
		if (!componentA.isVisited) {
			componentA.isVisited = true

			componentB.lastCheckpoint = componentA
			componentB.checkpointsVisited++

			BodyEdit.setColor(entityA, Rgba.PINK)

			if (componentB.checkpointsVisited == componentB.checkpointsTotal) {
				LevelManager.requestNextLevel()
			}
		}
	}
}