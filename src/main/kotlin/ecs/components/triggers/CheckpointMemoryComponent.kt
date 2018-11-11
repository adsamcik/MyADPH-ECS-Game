package ecs.components.triggers

import engine.component.IComponent

data class CheckpointMemoryComponent(
	var lastCheckpoint: CheckpointComponent,
	val checkpointsTotal: Int,
	var checkpointsVisited: Int = 0
) : IComponent