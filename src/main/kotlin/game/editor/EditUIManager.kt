package game.editor

import definition.Object
import engine.component.IComponent
import engine.component.IGeneratedComponent
import engine.entity.Entity
import extensions.createDiv
import extensions.createTitle3
import game.editor.EditUIUtility.createNumberEdit
import game.editor.EditUIUtility.createTextEdit
import game.editor.edit.component.BodyComponentEdit
import game.editor.edit.component.ModifierSpreaderComponentEdit
import game.editor.edit.template.IComponentEdit
import game.editor.edit.template.IObjectEdit
import org.w3c.dom.Element
import kotlin.browser.document

class EditUIManager {
	private val customEditorList = listOf<IComponentEdit<*>>(
		BodyComponentEdit(),
		ModifierSpreaderComponentEdit()
	)

	fun createUIFor(entity: Entity, component: IComponent): Element? {
		if (component is IGeneratedComponent) return null

		val container = document.createDiv()

		val customEditor = customEditorList.find { it.type.isInstance(component) }

		if (customEditor != null) {
			container.appendChild(document.createTitle3 { it.innerHTML = requireNotNull(component::class.simpleName) })

			@Suppress("unchecked_cast")
			customEditor as IComponentEdit<IComponent>
			container.appendChild(customEditor.onCreateEdit(entity, component))
		} else {
			val children = createEditForObject(component, requireNotNull(component::class.simpleName), 3)
				?: return null

			container.appendChild(children)
		}

		return container
	}

	companion object {
		fun requireEditForObject(value: dynamic, name: String, depth: Int, editData: CustomEditData? = null): Element =
			requireNotNull(createEditForObject(value, name, depth, editData))

		fun createEditForObject(
			value: dynamic,
			name: String,
			depth: Int,
			editData: CustomEditData? = null
		): Element? {
			if (value == null) return null

			val propertyClass = value::class
			val customEditor = editData?.editorList?.find { editor -> editor.type == propertyClass }

			if (customEditor != null) {
				return customEditor.onCreateEdit(editData.entity, value)
			}

			val result = Object.getOwnPropertyNames(value)

			val elements = result.mapNotNull {
				if (it.startsWith('_')) return null

				val property = value[it]

				when (property) {
					is String -> createTextEdit(value, property, it)
					is Number -> createNumberEdit(value, property, it)
					else -> {
						val typeOf = jsTypeOf(property as? Any)
						when {
							depth <= 0 -> null
							typeOf == "object" -> createEditForObject(property, it, depth - 1, editData)
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
	}

	data class CustomEditData(val entity: Entity, val editorList: List<IObjectEdit<dynamic>>, val parent: Element)
}