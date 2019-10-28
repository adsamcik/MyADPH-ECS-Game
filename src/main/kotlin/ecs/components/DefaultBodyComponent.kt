package ecs.components

import ecs.components.template.IBodyComponent
import engine.component.IComponent
import engine.physics.bodies.builder.IBodyBuilder
import kotlinx.serialization.Serializable

@Serializable
data class DefaultBodyComponent(override val value: IBodyBuilder) : IBodyComponent