package game.editor.edit.`object`

import ecs.components.BodyComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.physics.bodies.shapes.IShape
import extensions.createDiv
import game.editor.EditUIUtility
import game.editor.edit.component.TransformComponentEdit
import game.editor.edit.template.IObjectEdit
import game.modifiers.factory.ShapeModifierFactory
import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.reflect.KClass

class ShapeModifierObjectEdit : IObjectEdit<ShapeModifierFactory> {
	override val type: KClass<ShapeModifierFactory>
		get() = ShapeModifierFactory::class


	override fun onCreateEdit(
		entity: Entity,
		component: ShapeModifierFactory
	): Element {
		val parent = document.createDiv()
		val bodyBuilder = component.bodyBuilder ?: MutableBodyBuilder(Circle(1.0), BodyMotionType.Dynamic)
		component.bodyBuilder = bodyBuilder
		val wrapper = BuilderWrapper(bodyBuilder)

		listOf(
			EditUIUtility.createNumberEdit(component, component::timeLeft.name, EditUIUtility.DOUBLE_STEP),
			ShapeObjectEdit(wrapper::onShapeChanged).onCreateEdit(entity, bodyBuilder.shape),
			EditUIUtility.createEnumEdit<BodyMotionType>(bodyBuilder, bodyBuilder::motionType.name),
			document.createDiv { transformParent ->
				transformParent.appendChild(TransformComponentEdit().onCreateEdit(entity, bodyBuilder.transform))
			},
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::density.name, EditUIUtility.DOUBLE_STEP),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::restitution.name, EditUIUtility.DOUBLE_STEP),
			EditUIUtility.createNumberEdit(bodyBuilder, bodyBuilder::friction.name, EditUIUtility.DOUBLE_STEP),
			EditUIUtility.createCheckboxEdit(bodyBuilder, bodyBuilder::isSensor.name),
			EditUIUtility.createColorEdit(bodyBuilder, bodyBuilder.fillColor, bodyBuilder::fillColor.name)
		).forEach {
			parent.appendChild(it)
		}

		return parent
	}

	private class BuilderWrapper(private val bodyBuilder: MutableBodyBuilder) {
		fun onShapeChanged(entity: Entity, shape: IShape, isNewShape: Boolean) {
			if (isNewShape) {
				bodyBuilder.shape = shape
			}
		}
	}
}