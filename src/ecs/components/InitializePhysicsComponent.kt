package ecs.components

import engine.component.IComponent
import jslib.Matter

data class InitializePhysicsComponent(val world: Matter.World,
                                      val body: Matter.Body) : IComponent