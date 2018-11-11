package ecs.components

import engine.component.IComponent
import engine.physics.bodies.builder.IMutableBodyBuilder

data class BodyComponent(val value: IMutableBodyBuilder) : IComponent