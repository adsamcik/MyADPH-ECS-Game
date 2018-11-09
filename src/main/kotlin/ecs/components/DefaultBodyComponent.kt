package ecs.components

import engine.component.IComponent
import engine.physics.bodies.builder.IBodyBuilder

data class DefaultBodyComponent(val value: IBodyBuilder) : IComponent