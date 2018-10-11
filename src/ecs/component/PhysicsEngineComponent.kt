package ecs.component

import Matter
import engine.component.IComponent

data class PhysicsEngineComponent(val engine: Matter.Engine) : IComponent