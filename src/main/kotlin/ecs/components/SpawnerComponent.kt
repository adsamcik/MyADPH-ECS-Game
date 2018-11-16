package ecs.components

import engine.component.IComponent
import engine.physics.bodies.builder.IMutableBodyBuilder
import game.levels.EntityCreator

data class SpawnerComponent(
	val bodyBuilder: IMutableBodyBuilder,
	val spawnDeltaTime: Double,
	val spawnFunction: EntityCreator.(IMutableBodyBuilder) -> Unit,
	var time: Double = 0.0
) : IComponent