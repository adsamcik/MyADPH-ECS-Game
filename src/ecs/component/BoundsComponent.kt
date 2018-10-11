package ecs.component

import engine.component.IComponent
import engine.physics.Bounds

data class BoundsComponent(val value: Bounds) : IComponent