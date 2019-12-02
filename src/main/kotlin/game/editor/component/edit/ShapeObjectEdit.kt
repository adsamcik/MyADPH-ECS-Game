package game.editor.component.edit

import engine.entity.Entity
import engine.physics.bodies.BodyEdit
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.shapes.Rectangle
import extensions.createElementTyped
import extensions.removeAllChildren
import game.editor.EditUIUtility
import game.editor.component.edit.template.IObjectEdit
import org.w3c.dom.Element
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.Option
import kotlin.browser.document
import kotlin.reflect.KClass

class ShapeObjectEdit(private val onShapeChanged: (entity: Entity, shape: IShape, isNewShape: Boolean) -> Unit) : IObjectEdit<IShape> {
	override val type: KClass<IShape>
		get() = IShape::class

	override fun onCreateEdit(entity: Entity, component: IShape, parent: Element) {
		val shapeList = listOf(Circle(5), Rectangle(10, 10))

		val optionList =
			shapeList.mapIndexed { index, shape -> Option(requireNotNull(shape::class.simpleName), index.toString()) }

		val select = document.createElementTyped<HTMLSelectElement>("select")

		optionList.forEach { option -> select.options.add(option) }

		val currentShape = requireNotNull(component::class.simpleName)
		val selectedIndex = optionList.indexOfFirst { it.text == currentShape }
		if (selectedIndex >= 0) {
			select.selectedIndex = selectedIndex
		}

		parent.appendChild(select)
		val shapeOptionsParent = document.createElement("div").also {
			parent.appendChild(it)
		}

		select.onchange = {
			val target = it.target as HTMLSelectElement
			val shape = shapeList[target.selectedIndex]
			onShapeChanged(entity, shape, true)
			updateShapeOptions(entity, shape, shapeOptionsParent)
			Unit
		}

		updateShapeOptions(entity, component, shapeOptionsParent)
	}

	private fun updateShapeOptions(entity: Entity, shape: IShape, parent: Element) {
		parent.removeAllChildren()
		val shapeData = ShapeDataWrap(entity)
		val list = when (shape) {
			is Rectangle -> listOf(
				EditUIUtility.createNumberEdit(
					shape, shape::width.name,
					EditUIUtility.DOUBLE_STEP, shapeData::setShapeValue
				),
				EditUIUtility.createNumberEdit(
					shape, shape::height.name,
					EditUIUtility.DOUBLE_STEP, shapeData::setShapeValue
				)
			)
			is Circle -> listOf(
				EditUIUtility.createNumberEdit(
					shape, shape::radius.name,
					EditUIUtility.DOUBLE_STEP, shapeData::setShapeValue
				)
			)
			else -> emptyList()
		}

		list.forEach { parent.appendChild(it) }
	}

	inner class ShapeDataWrap(private val entity: Entity) {
		fun setShapeValue(sourceObject: dynamic, propertyName: String, newValue: Double) {
			sourceObject[propertyName] = newValue
			onShapeChanged(entity, sourceObject as IShape, false)
		}
	}
}