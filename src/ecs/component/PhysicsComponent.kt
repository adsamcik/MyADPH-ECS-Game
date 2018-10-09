package ecs.component

import engine.component.IComponent

data class PhysicsComponent(var friction: Double, var affectedByGravity: Boolean, var mass: Double) : IComponent