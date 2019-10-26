package game.editor

import definition.Object
import engine.component.IComponent
import engine.entity.Entity
import extensions.createDiv
import extensions.createInput
import extensions.createTitle3
import game.editor.component.BodyComponentEdit
import game.editor.component.IComponentEdit
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document

class EditUIManager {
	val customEditorList = listOf<IComponentEdit<*>>(BodyComponentEdit())


	private fun createTextEdit(parent: dynamic, value: dynamic, name: String): Element = document.createDiv { wrapper ->
		wrapper.appendChild(document.createElement("p").apply {
			innerHTML = name
		})
		wrapper.appendChild(document.createInput {
			it as HTMLInputElement
			it.oninput = { input ->
				parent[name] = (input.target as HTMLInputElement).value
				null
			}
			it.defaultValue = value.toString()
		})
	}


	private fun createNumberEdit(parent: dynamic, value: dynamic, name: String): Element =
		document.createDiv { wrapper ->
			wrapper.appendChild(document.createElement("p").apply {
				innerHTML = name
			})
			wrapper.appendChild(document.createInput {
				it as HTMLInputElement
				it.oninput = { input ->
					parent[name] = (input.target as HTMLInputElement).value.toDouble()
					null
				}
				it.type = "number"
				it.defaultValue = value.toString()
			})
		}

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
		val container = document.createDiv()

		val valueType = requireNotNull(component::class.simpleName)
		val customEditor = customEditorList.find { valueType == it.componentName }

		if (customEditor != null) {
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