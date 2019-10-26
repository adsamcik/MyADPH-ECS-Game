@file:Suppress("DuplicatedCode")

package game.editor

import extensions.createDiv
import extensions.createElementTyped
import extensions.createInput
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import kotlin.browser.document

object EditUIUtility {
	private fun createTitle(name: String): Element =
		document.createElement("p").apply {
			innerHTML = name
		}

	fun createTextEdit(parent: dynamic, value: dynamic, name: String): Element = document.createDiv { wrapper ->
		wrapper.appendChild(createTitle(name))
		wrapper.appendChild(document.createInput {
			it as HTMLInputElement
			it.oninput = { input ->
				parent[name] = (input.target as HTMLInputElement).value
				null
			}
			it.defaultValue = value.toString()
		})
	}

	fun createCheckboxEdit(dataObject: dynamic, name: String): Element =
		createCheckboxEdit(dataObject, dataObject[name], name)

	fun createCheckboxEdit(dataObject: dynamic, value: dynamic, name: String): Element =
		document.createDiv { wrapper ->
			wrapper.appendChild(createTitle(name))
			wrapper.appendChild(document.createInput {
				it as HTMLInputElement
				it.onchange = { input ->
					dataObject[name] = (input.target as HTMLInputElement).checked
					null
				}
				it.type = "checkbox"
				if(value != null) {
					it.checked = value as Boolean
				}
			})
		}

	fun createNumberEdit(dataObject: dynamic, name: String, step: Double = 1.0): Element =
		createNumberEdit(dataObject, dataObject[name], name, step)

	fun createNumberEdit(dataObject: dynamic, value: dynamic, name: String, step: Double = 1.0): Element =
		document.createDiv { wrapper ->
			wrapper.appendChild(createTitle(name))
			wrapper.appendChild(document.createInput {
				it as HTMLInputElement
				it.oninput = { input ->
					dataObject[name] = (input.target as HTMLInputElement).value.toDouble()
					null
				}
				it.type = "number"
				it.step = step.toString()
				if (value != null) {
					it.defaultValue = value.toString()
				}
			})
		}
}