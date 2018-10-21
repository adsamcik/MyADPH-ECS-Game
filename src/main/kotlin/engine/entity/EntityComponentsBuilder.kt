package engine.entity

import engine.component.IComponent

class EntityComponentsBuilder {
	private val _components = mutableListOf<IComponent>()

	val components: Collection<IComponent>
		get() = _components

	fun addComponent(component: IComponent) {
		_components.add(component)
	}
}