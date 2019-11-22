package game.editor.component

import engine.component.IComponent
import general.Double2

data class CheckpointDefinitionComponent(val orderNumber: Int, val respawnPosition: Double2) : IComponent