package ecs.components

import engine.component.IComponent
import jslib.Matter

data class PhysicsEngineComponent(val value: Matter.Engine) : IComponent