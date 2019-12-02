package game.editor.edit.component

import ecs.components.BodyComponent
import ecs.components.template.IBodyComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.bodies.BodyEdit
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.IShape
import extensions.createDiv
import game.editor.EditUIUtility
import game.editor.EditUIUtility.DOUBLE_STEP
import game.editor.edit.`object`.ShapeObjectEdit
import game.editor.edit.template.IComponentEdit
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.reflect.KClass

class BodyComponentEdit : IComponentEdit<IBodyComponent> {
	override val type: KClass<IBodyComponent> = IBodyComponent::class

	private fun onShapeChanged(entity: Entity, shape: IShape, isNewShape: Boolean) {
		if (isNewShape) {
			BodyEdit.setShape(entity, shape)
		} else {
			BodyEdit.updateShape(entity)
		}
	}


	override fun onCreateEdit(
		entity: Entity,
		component: IBodyComponent
	): Element {
		val parent = document.createDiv()
		val bodyBuilder = MutableBodyBuilder(component.value)
		EntityManager.setComponent(entity, BodyComponent(bodyBuilder))

		listOf(
			ShapeObjectEdit(this::onShapeChanged).onCreateEdit(entity, bodyBuilder.shape),
			EditUIUtility.createEnumEdit<BodyMotionType>(bodyBuilder, bodyBuilder::motionType.name),
			document.createDiv { transformParent ->
				transformParent.appendChild(TransformComponentEdit().onCreateEdit(entity, bodyBuilder.transform))
			},
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::density.name, DOUBLE_STEP),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::restitution.name, DOUBLE_STEP),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::friction.name, DOUBLE_STEP),
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

		return parent
	}
}