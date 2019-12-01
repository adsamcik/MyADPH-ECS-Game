package game.editor.component.edit

import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.types.Transform
import game.editor.EditUIUtility
import game.editor.component.edit.template.IObjectEdit
import general.Double2
import org.w3c.dom.Element
import kotlin.reflect.KClass

class TransformComponentEdit : IObjectEdit<Transform> {
	override val type: KClass<Transform>
		get() = Transform::class

	override fun onCreateEdit(entity: Entity, component: Transform, parent: Element) {
		val indent = EditUIUtility.createIndentWithTitle(parent, "Transform")
		listOf(
			EditUIUtility.createNumberEdit(
				component.position,
				component.position::x.name,
				EditUIUtility.QUARTER_STEP
			) { sourceObject, propertyName, newValue ->
				sourceObject[propertyName] = newValue
				entity.getComponent<PhysicsEntityComponent>().body.position = sourceObject as Double2
			},
			EditUIUtility.createNumberEdit(
				component.position,
				component.position::y.name,
				EditUIUtility.QUARTER_STEP
			) { sourceObject, propertyName, newValue ->
				sourceObject[propertyName] = newValue
				entity.getComponent<PhysicsEntityComponent>().body.position = sourceObject as Double2
			},
			EditUIUtility.createNumberEdit(
				component, component::angleDegrees.name,
				EditUIUtility.INT_STEP
			)
			{ sourceObject, propertyName, newValue ->
				sourceObject[propertyName] = newValue
				entity.getComponent<PhysicsEntityComponent>().body.angle = newValue
			}
		).forEach {
			indent.appendChild(it)
		}
	}

}