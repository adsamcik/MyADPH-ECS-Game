package ecs.components

import engine.component.IComponent
import engine.physics.bodies.builder.IBodyBuilder

data class SpawnerComponent(val bodyBuilder: IBodyBuilder, val spawnDeltaTime: Double, var time: Double = 0.0) : IComponent