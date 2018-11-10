package ecs.components.triggers

import engine.component.IComponent
import utility.Double2

data class CheckpointComponent(val id: Int, val respawnPosition: Double2, val type: CheckpointType) : IComponent

enum class CheckpointType {
	Start,
	Standard,
	Limited,
	End
}