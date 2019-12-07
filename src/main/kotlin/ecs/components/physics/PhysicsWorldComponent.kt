package ecs.components.physics

import engine.component.IGeneratedComponent
import engine.physics.engines.PhysicsEngine

data class PhysicsWorldComponent(val engine: PhysicsEngine) : IGeneratedComponent