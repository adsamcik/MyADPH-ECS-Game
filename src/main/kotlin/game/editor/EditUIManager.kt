package game.editor

import definition.Object
import engine.component.IComponent
import engine.component.IGeneratedComponent
import engine.entity.Entity
import extensions.createDiv
import extensions.createTitle3
import game.editor.EditUIUtility.createNumberEdit
import game.editor.EditUIUtility.createTextEdit
import game.editor.component.edit.BodyComponentEdit
import game.editor.component.edit.IComponentEdit
import org.w3c.dom.Element
import kotlin.browser.document

class EditUIManager {
	private val customEditorList = listOf<IComponentEdit<*>>(BodyComponentEdit())

	private fun createEditForObject(value: dynamic, name: String, depth: Int): Element? {
		if (value == null) return null

		val result = Object.getOwnPropertyNames(value)

		val elements = result.mapNotNull {
			if (it.startsWith('_')) return null

			when (val property = value[it]) {
				is String -> createTextEdit(value, property, it)
				is Number -> createNumberEdit(value, property, it)
				else -> {
					val typeOf = jsTypeOf(property as? Any)
					when {
						depth <= 0 -> null
						typeOf == "object" -> createEditForObject(property, it, depth - 1)
						else -> null
					}
				}
			}
		}

		return if (elements.isNotEmpty()) {
			val container = document.createDiv()
			container.appendChild(document.createTitle3 { it.innerHTML = name })
			elements.forEach { container.appendChild(it) }
			container
		} else {
			null
		}
	}

	fun createUIFor(entity: Entity, component: IComponent): Element? {
		if (component is IGeneratedComponent) return null

		val container = document.createDiv()

		val customEditor = customEditorList.find { it.type.isInstance(component) }

		if (customEditor != null) {
			container.appendChild(document.createTitle3 { it.innerHTML = requireNotNull(component::class.simpleName) })

			@Suppress("unchecked_cast")
			customEditor as IComponentEdit<IComponent>
			customEditor.onCreateEdit(entity, component, container)
		} else {
			val children = createEditForObject(component, requireNotNull(component::class.simpleName), 3)
				?: return null

			container.appendChild(children)
		}

		return container
	}
}