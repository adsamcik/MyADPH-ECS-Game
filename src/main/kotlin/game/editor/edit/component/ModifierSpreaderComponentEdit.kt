package game.editor.edit.component

import ecs.components.modifiers.ModifierSpreaderComponent
import engine.entity.Entity
import extensions.addOnClickListener
import extensions.createButton
import extensions.createDiv
import extensions.format
import game.editor.EditUIManager
import game.editor.EditUIUtility
import game.editor.edit.`object`.ShapeModifierObjectEdit
import game.editor.edit.template.IComponentEdit
import game.modifiers.factory.*
import game.modifiers.factory.template.IModifierFactory
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.reflect.KClass

class ModifierSpreaderComponentEdit : IComponentEdit<ModifierSpreaderComponent> {
	override val type: KClass<ModifierSpreaderComponent>
		get() = ModifierSpreaderComponent::class

	private val factoryList =
		listOf(
			{ MaxEnergyModifierFactory() },
			{ AccelerationModifierFactory() },
			{ MaxHealthModifierFactory() },
			{ ShapeModifierFactory() },
			{ ClearModifierFactory() }
		)

	private val editorList =
		listOf(ShapeModifierObjectEdit())

	private fun addModifierEdit(
		entity: Entity,
		parent: Element,
		beforeElement: Element,
		modifierFactory: IModifierFactory
	) {
		val editData = EditUIManager.CustomEditData(entity, editorList, parent)
		EditUIManager.createEditForObject(modifierFactory, modifierFactory::class.js.name, 1, editData)?.also { elem ->
			parent.insertBefore(elem, beforeElement)
		}
	}

	override fun onCreateEdit(
		entity: Entity,
		component: ModifierSpreaderComponent
	): Element {
		val parent = document.createDiv { }
		val indent = EditUIUtility.createIndentWithTitle(parent, "Available modifiers")

		val availableModifiers = factoryList.filter { factoryMaker ->
			val factory = factoryMaker()
			component.factory.commandCollection.none { it::class == factory::class }
		}

		availableModifiers.forEach { modifierFactoryMaker ->
			document.createButton { button ->
				button.className = "ui-button"
				val name = modifierFactoryMaker()::class.js.name.removeSuffix("ModifierFactory").format()
				button.textContent = "Add $name"
				button.addOnClickListener {
					modifierFactoryMaker().also { factory ->
						component.factory.addModifier(factory)
						addModifierEdit(entity, parent, indent, factory)
					}
					indent.removeChild(button)
				}

				indent.appendChild(button)
			}
		}

		component.factory.commandCollection.forEach { addModifierEdit(entity, parent, indent, it) }

		return parent
	}

}