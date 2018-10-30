package ecs.components.physics

import engine.component.IComponent
import engine.physics.engines.PhysicsEngine

data class PhysicsUpdateComponent(val engine: PhysicsEngine) : IComponent