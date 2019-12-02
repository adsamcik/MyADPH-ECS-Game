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
import extensions.createDiv
import extensions.createElementTyped
import extensions.removeAllChildren
import game.editor.EditUIManager
import game.editor.EditUIUtility
import game.editor.EditUIUtility.DOUBLE_STEP
import game.editor.component.edit.template.IComponentEdit
import org.w3c.dom.Element
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.Option
import kotlin.browser.document
import kotlin.reflect.KClass

class BodyComponentEdit : IComponentEdit<IBodyComponent> {
	override val type: KClass<IBodyComponent> = IBodyComponent::class

	private fun onShapeChanged(entity: Entity, shape: IShape, isNewShape: Boolean) {
		console.log(entity, shape, isNewShape)
		if (isNewShape) {
			BodyEdit.setShape(entity, shape)
		} else {
			BodyEdit.updateShape(entity)
		}
	}

	override fun onCreateEdit(entity: Entity, component: IBodyComponent, parent: Element) {
		ShapeObjectEdit(this::onShapeChanged).onCreateEdit(entity, component.value.shape, parent)
		val bodyBuilder = MutableBodyBuilder(component.value)
		EntityManager.setComponent(entity, BodyComponent(bodyBuilder))

		listOf(
			EditUIUtility.createEnumEdit<BodyMotionType>(bodyBuilder, bodyBuilder::motionType.name),
			document.createDiv { transformParent ->
				TransformComponentEdit().onCreateEdit(entity, component.value.transform, transformParent)
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
	}
}