package game.editor.component

import engine.component.IComponent
import engine.entity.Entity
import org.w3c.dom.Element

interface IComponentEdit<T : IComponent> {
	val componentName: String
	fun onCreateEdit(entity: Entity, component: T, parent: Element)
}