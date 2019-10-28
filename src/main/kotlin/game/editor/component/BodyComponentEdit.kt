package game.editor.component

import ecs.components.BodyComponent
import ecs.components.template.IBodyComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.bodies.BodyEdit
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.Rectangle
import extensions.addOnClickListener
import extensions.createElementTyped
import game.editor.EditUIUtility
import org.w3c.dom.Element
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.Option
import kotlin.browser.document
import kotlin.reflect.KClass

class BodyComponentEdit : IComponentEdit<IBodyComponent> {
	override val type: KClass<IBodyComponent> = IBodyComponent::class

	private fun addShapeSelect(entity: Entity, component: IBodyComponent, parent: Element) {
		val shapeList = listOf(Circle(10), Rectangle(10, 10))
		val optionList =
			shapeList.mapIndexed { index, shape -> Option(requireNotNull(shape::class.simpleName), index.toString()) }

		val select = document.createElementTyped<HTMLSelectElement>("select")

		optionList.forEach { option -> select.options.add(option) }

		val currentShape = requireNotNull(component.value.shape::class.simpleName)
		val selectedIndex = optionList.indexOfFirst { it.text == currentShape }
		if (selectedIndex >= 0) {
			select.selectedIndex = selectedIndex
		}

		select.onchange = {
			val target = it.target as HTMLSelectElement
			val shape = shapeList[target.selectedIndex]
			BodyEdit.setShape(entity, shape)
			Unit
		}

		parent.appendChild(select)
	}

	override fun onCreateEdit(entity: Entity, component: IBodyComponent, parent: Element) {
		addShapeSelect(entity, component, parent)
		val bodyBuilder = MutableBodyBuilder(component.value)
		EntityManager.setComponent(entity, BodyComponent(bodyBuilder))
		val transform = bodyBuilder.transform

		listOf(
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::density.name, 0.01),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::restitution.name, 0.01),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::friction.name, 0.01),
			EditUIUtility.createNumberEdit(transform, transform::angleDegrees.name, 0.01),
			EditUIUtility.createCheckboxEdit(bodyBuilder.isSensor, bodyBuilder::isSensor.name)
		).forEach {
			parent.appendChild(it)
		}
	}

}