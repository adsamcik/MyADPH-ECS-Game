package ecs.components.triggers

import engine.component.IComponent
import utility.Double2

data class CheckpointComponent(val id: Int, val respawnPosition: Double2, val type: CheckpointType, var isVisited: Boolean = false) : IComponent

enum class CheckpointType {
	Start,
	Standard,
	Limited,
	End
}