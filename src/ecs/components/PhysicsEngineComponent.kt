package ecs.components

import Matter
import engine.component.IComponent

data class PhysicsEngineComponent(val value: Matter.Engine) : IComponent