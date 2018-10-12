package ecs.component

import Matter
import engine.component.IComponent
import engine.physics.IShape
import Render

data class InitializePhysicsComponent(val world: Matter.World,
                                      val shape: IShape,
                                      val render: Render = Render(),
                                      val static: Boolean = false) : IComponent