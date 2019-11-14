package ecs.components

import engine.component.IComponent
import kotlinx.serialization.Serializable

@Serializable
data class LifeTimeComponent(var timeLeft: Double) : IComponent