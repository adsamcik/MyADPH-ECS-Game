package ecs.component

import Matter
import engine.component.IComponent

data class PhysicsEngineComponent(val value: Matter.Engine) : IComponent