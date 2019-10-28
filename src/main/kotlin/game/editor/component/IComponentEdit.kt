package game.editor.component

import engine.component.IComponent
import engine.entity.Entity
import org.w3c.dom.Element
import kotlin.reflect.KClass

interface IComponentEdit<T : IComponent> {
	val type: KClass<T>
	fun onCreateEdit(entity: Entity, component: T, parent: Element)
}