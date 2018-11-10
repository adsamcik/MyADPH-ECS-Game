package ecs.components.triggers

import engine.component.IComponent

data class CheckpointMemoryComponent(var lastCheckpoint: CheckpointComponent) : IComponent