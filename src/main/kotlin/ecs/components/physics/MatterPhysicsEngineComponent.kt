package ecs.components.physics

import engine.component.IComponent
import jslib.Matter

data class MatterPhysicsEngineComponent(val value: Matter.Engine) : IComponent