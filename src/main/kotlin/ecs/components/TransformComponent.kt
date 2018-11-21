package ecs.components

import engine.component.IComponent
import general.Double2

data class TransformComponent(var position: Double2, var angleRadians: Double) : IComponent