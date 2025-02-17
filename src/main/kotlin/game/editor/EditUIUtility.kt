@file:Suppress("DuplicatedCode")

package game.editor

import engine.types.Rgba
import extensions.createDiv
import extensions.createElementTyped
import extensions.createInput
import org.w3c.dom.*
import kotlin.browser.document
import kotlin.math.min

object EditUIUtility {
	const val QUARTER_STEP = 0.25
	const val DOUBLE_STEP = 0.01
	const val INT_STEP = 1.0

	private fun createTitle(name: String, depth: Int = 0): Element =
		document.createElement("h${min(depth + 1, 6)}").apply {
			innerHTML = name
		}

	private fun createInputWrapper(name: String, fieldCreator: () -> Element) =
		document.createDiv { wrapper ->
			wrapper.appendChild(createTitle(name, 1))
			wrapper.appendChild(fieldCreator())
		}

	fun createTextEdit(parent: dynamic, value: dynamic, name: String): Element = createInputWrapper(name) {
		document.createInput {
			it.oninput = { input ->
				parent[name] = (input.target as HTMLInputElement).value
				null
			}
			it.defaultValue = value.toString()
		}
	}

	fun createIndentWithTitle(parentElement: Element, title: String): Element =
		createIndent(parentElement).apply {
			appendChild(createTitle(title, 2))
		}


	fun createIndent(parentElement: Element): Element =
		document.createDiv {
			it.className = "ui-indent"
			parentElement.appendChild(it)
		}

	fun createCheckboxEdit(dataObject: dynamic, name: String): Element =
		createCheckboxEdit(dataObject, dataObject[name], name)

	fun createCheckboxEdit(dataObject: dynamic, value: dynamic, name: String): Element =
		createInputWrapper(name) {
			document.createInput {
				it.onchange = { input ->
					dataObject[name] = (input.target as HTMLInputElement).checked
					null
				}
				it.type = "checkbox"
				if (value != null) {
					it.checked = value as Boolean
				}
			}
		}

	fun createNumberEdit(dataObject: dynamic, name: String, step: Double = INT_STEP): Element =
		createNumberEdit(dataObject, dataObject[name], name, step)

	fun createNumberEdit(dataObject: dynamic, value: dynamic, name: String, step: Double = INT_STEP): Element =
		createNumberEdit(dataObject, value, name, step) { sourceObject, propertyName, newValue ->
			sourceObject[propertyName] = newValue
		}

	fun createNumberEdit(
		dataObject: dynamic,
		name: String,
		step: Double,
		onChange: (sourceObject: dynamic, propertyName: String, newValue: Double) -> Unit
	) = createNumberEdit(dataObject, dataObject[name], name, step, onChange)

	fun createNumberEdit(
		dataObject: dynamic,
		value: dynamic,
		name: String,
		step: Double,
		onChange: (sourceObject: dynamic, propertyName: String, newValue: Double) -> Unit
	) = createInputWrapper(name) {
		document.createInput {
			it.oninput = { input ->
				val inputValue = (input.target as HTMLInputElement).valueAsNumber
				onChange(dataObject, name, inputValue)
			}
			it.type = "number"
			it.step = step.toString()
			if (value != null) {
				it.defaultValue = value.toString()
			}
		}
	}

	fun createColorEdit(
		dataObject: dynamic,
		name: String,
		onChange: (sourceObject: dynamic, propertyName: String, newValue: Rgba) -> Unit
	) =
		createColorEdit(dataObject, dataObject[name] as? Rgba, name, onChange)

	fun createColorEdit(dataObject: dynamic, value: dynamic, name: String) =
		createColorEdit(dataObject, value as? Rgba, name) { sourceObject, propertyName, newValue ->
			sourceObject[propertyName] = newValue
		}

	fun createColorEdit(
		dataObject: dynamic,
		value: Rgba?,
		name: String,
		onChange: (sourceObject: dynamic, propertyName: String, newValue: Rgba) -> Unit
	) = createInputWrapper(name) {
		document.createInput {
			it.type = "color"
			if (value != null) {
				it.value = value.rgbHex
			}

			it.oninput = { input ->
				val inputValue = (input.target as HTMLInputElement).value.removePrefix("#")
				val rgba = Rgba(inputValue.toUInt(16).shl(8) + 255U)
				onChange(dataObject, name, rgba)
			}
		}
	}

	inline fun <reified E : Enum<E>> createEnumEdit(
		dataObject: dynamic,
		name: String
	) = createEnumEdit<E>(dataObject, name) { sourceObject, propertyName, newValue ->
		sourceObject[propertyName] = newValue
	}

	inline fun <reified E : Enum<E>> createEnumEdit(
		dataObject: dynamic,
		name: String,
		noinline onChange: (sourceObject: dynamic, propertyName: String, newValue: E) -> Unit
	) = createEnumEdit(dataObject, dataObject[name] as E, enumValues(), name) { sourceObject, propertyName, newValue ->
		val enumValue = enumValueOf<E>(newValue)
		onChange(sourceObject, propertyName, enumValue)
	}

	fun <E : Enum<E>> createEnumEdit(
		dataObject: dynamic,
		value: E,
		values: Array<E>,
		name: String,
		onChange: (sourceObject: dynamic, propertyName: String, newValue: String) -> Unit
	) = createInputWrapper(name) {
		document.createElementTyped<HTMLSelectElement>("select") {
			values.map { item ->
				Option(item.name)
			}.forEach { enumValue ->
				it.options.add(enumValue)
			}

			it.selectedIndex = values.indexOf(value)

			it.onchange = { event ->
				val target = event.target as HTMLSelectElement
				onChange(dataObject, name, target.value)
			}
		}
	}
}