package ecs.components.physics

import engine.component.IGeneratedComponent
import engine.physics.engines.PhysicsEngine

data class PhysicsUpdateComponent(val engine: PhysicsEngine) : IGeneratedComponent