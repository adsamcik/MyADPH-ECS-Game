package ecs.components

import engine.component.IComponent
import general.Double2
import kotlinx.serialization.Serializable

@Serializable
data class AccelerationComponent(var value: Double2) : IComponent