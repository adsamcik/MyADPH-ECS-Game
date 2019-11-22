package ecs.components.triggers

import engine.component.IComponent
import general.Double2
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckpointComponent(val id: Int, val respawnPosition: Double2, val type: CheckpointType, var isVisited: Boolean = false) : IComponent

@Serializable
enum class CheckpointType {
	Start,
	Standard,
	Limited,
	End
}