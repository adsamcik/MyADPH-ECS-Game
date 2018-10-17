package ecs.components.modifiers

import engine.component.IComponent
import game.modifiers.ModifierCommandFactory

data class ModifierSpreaderComponent(val factory: ModifierCommandFactory) : IComponent