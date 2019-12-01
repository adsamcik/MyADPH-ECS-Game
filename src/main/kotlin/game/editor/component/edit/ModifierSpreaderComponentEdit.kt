package game.editor.component.edit

import ecs.components.modifiers.ModifierSpreaderComponent
import engine.entity.Entity
import extensions.addOnClickListener
import extensions.createButton
import extensions.format
import game.editor.EditUIManager
import game.editor.EditUIUtility
import game.editor.component.edit.template.IComponentEdit
import game.modifiers.IModifierFactory
import game.modifiers.MaxEnergyModifierFactory
import game.modifiers.ShapeModifierFactory
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.reflect.KClass

class ModifierSpreaderComponentEdit : IComponentEdit<ModifierSpreaderComponent> {
	override val type: KClass<ModifierSpreaderComponent>
		get() = ModifierSpreaderComponent::class

	private val factoryList = listOf({ MaxEnergyModifierFactory() }, { ShapeModifierFactory() })

	private fun addModifierEdit(parent: Element, beforeElement: Element, modifierFactory: IModifierFactory) {
		EditUIManager.createEditForObject(modifierFactory, modifierFactory::class.js.name, 1)?.also { elem ->
			parent.insertBefore(elem, beforeElement)
		}
	}

	override fun onCreateEdit(entity: Entity, component: ModifierSpreaderComponent, parent: Element) {
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
						addModifierEdit(parent, indent, factory)
					}
					indent.removeChild(button)
				}

				indent.appendChild(button)
			}
		}

		component.factory.commandCollection.forEach { addModifierEdit(parent, indent, it) }
	}

}