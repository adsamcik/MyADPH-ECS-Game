package ecs.component

import engine.component.IComponent

data class PhysicsComponent(var friction: Double, var gravity: Double) : IComponent