package ecs.component

import Matter
import engine.component.IComponent

data class InitializePhysicsComponent(val world: Matter.World,
                                      val body: Matter.Body) : IComponent