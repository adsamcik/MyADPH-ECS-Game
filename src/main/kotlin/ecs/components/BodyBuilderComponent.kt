package ecs.components

import engine.component.IComponent
import engine.physics.bodies.BodyBuilder

data class BodyBuilderComponent(val bodyBuilder: BodyBuilder) : IComponent