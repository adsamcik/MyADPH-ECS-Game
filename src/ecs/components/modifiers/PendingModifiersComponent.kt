package ecs.components.modifiers

import engine.component.IComponent
import game.modifiers.ModifierCommandFactory

class PendingModifiersComponent(vararg factories: ModifierCommandFactory) : IComponent {
	private val _factories = mutableListOf(*factories)

	val factories: Collection<ModifierCommandFactory>
		get() = _factories

	fun addFactory(factory: ModifierCommandFactory) = _factories.add(factory)
}