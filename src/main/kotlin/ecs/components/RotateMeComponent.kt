package ecs.components

import engine.component.IComponent
import kotlinx.serialization.Serializable

@Serializable
data class RotateMeComponent(var speed: Double) : IComponent