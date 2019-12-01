package ecs.components.modifiers

import engine.component.IComponent
import game.modifiers.ModifierCommandFactory
import kotlinx.serialization.Serializable

@Serializable
data class ModifierSpreaderComponent(val factory: ModifierCommandFactory) : IComponent