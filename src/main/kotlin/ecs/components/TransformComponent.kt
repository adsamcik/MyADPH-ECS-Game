package ecs.components

import engine.component.IComponent
import utility.Double2

data class TransformComponent(var position: Double2, var angleRadians: Double) : IComponent