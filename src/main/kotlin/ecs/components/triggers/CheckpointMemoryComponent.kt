package ecs.components.triggers

import engine.component.IGeneratedComponent

data class CheckpointMemoryComponent(
	var lastCheckpoint: CheckpointComponent,
	val checkpointsTotal: Int,
	var checkpointsVisited: Int = 0
) : IGeneratedComponent