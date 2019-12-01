package game.editor.component.edit

import ecs.components.BodyComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.template.IBodyComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.bodies.BodyEdit
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.shapes.Rectangle
import extensions.createElementTyped
import extensions.removeAllChildren
import game.editor.EditUIUtility
import org.w3c.dom.Element
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.Option
import kotlin.browser.document
import kotlin.reflect.KClass

class BodyComponentEdit : IComponentEdit<IBodyComponent> {
	override val type: KClass<IBodyComponent> = IBodyComponent::class

	private var shapeOptionsParent: Element? = null

	private fun addShapeSelect(entity: Entity, component: IBodyComponent, parent: Element) {
		val shapeList = listOf(Circle(5), Rectangle(10, 10))
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
			updateShapeOptions(entity, shape)
			Unit
		}

		parent.appendChild(select)
		shapeOptionsParent = document.createElement("div").also {
			parent.appendChild(it)
		}

		updateShapeOptions(entity, component.value.shape)
	}

	override fun onCreateEdit(entity: Entity, component: IBodyComponent, parent: Element) {
		addShapeSelect(entity, component, parent)
		val bodyBuilder = MutableBodyBuilder(component.value)
		EntityManager.setComponent(entity, BodyComponent(bodyBuilder))
		val transform = bodyBuilder.transform

		listOf(
			EditUIUtility.createEnumEdit<BodyMotionType>(bodyBuilder, bodyBuilder::motionType.name),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::density.name, DOUBLE_STEP),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::restitution.name, DOUBLE_STEP),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::friction.name, DOUBLE_STEP),
			EditUIUtility.createNumberEdit(transform, transform::angleDegrees.name, DOUBLE_STEP)
			{ sourceObject, propertyName, newValue ->
				sourceObject[propertyName] = newValue
				entity.getComponent<PhysicsEntityComponent>().body.angle = newValue
			},
			EditUIUtility.createCheckboxEdit(bodyBuilder, bodyBuilder::isSensor.name),
			EditUIUtility.createColorEdit(
				bodyBuilder,
				bodyBuilder.fillColor,
				bodyBuilder::fillColor.name
			) { _, _, newValue ->
				BodyEdit.setColor(entity, newValue)
			}
		).forEach {
			parent.appendChild(it)
		}
	}

	private fun updateShapeOptions(entity: Entity, shape: IShape) {
		val parent = requireNotNull(shapeOptionsParent)
		parent.removeAllChildren()
		val shapeData = ShapeDataWrap(entity)
		val list = when (shape) {
			is Rectangle -> listOf(
				EditUIUtility.createNumberEdit(
					shape, shape::width.name,
					DOUBLE_STEP, shapeData::setShapeValue
				),
				EditUIUtility.createNumberEdit(
					shape, shape::height.name,
					DOUBLE_STEP, shapeData::setShapeValue
				)
			)
			is Circle -> listOf(
				EditUIUtility.createNumberEdit(
					shape, shape::radius.name,
					DOUBLE_STEP, shapeData::setShapeValue
				)
			)
			else -> emptyList()
		}

		list.forEach { parent.appendChild(it) }
	}

	class ShapeDataWrap(private val entity: Entity) {
		fun setShapeValue(sourceObject: dynamic, propertyName: String, newValue: Double) {
			sourceObject[propertyName] = newValue
			BodyEdit.updateShape(entity)
		}
	}

	companion object {
		private const val DOUBLE_STEP = 0.01
	}

}