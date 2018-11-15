package ecs.components

import engine.component.IComponent
import engine.physics.bodies.builder.IBodyBuilder

data class BodyComponent(val value: IBodyBuilder) : IComponent