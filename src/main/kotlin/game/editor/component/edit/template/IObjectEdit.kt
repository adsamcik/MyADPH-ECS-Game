package game.editor.component.edit.template

import engine.entity.Entity
import org.w3c.dom.Element
import kotlin.reflect.KClass

interface IObjectEdit<T : Any> {
	val type: KClass<T>
	fun onCreateEdit(entity: Entity, component: T, parent: Element)
}