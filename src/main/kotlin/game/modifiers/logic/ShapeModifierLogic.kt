package game.modifiers.logic

import ecs.components.DefaultBodyComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.interfaces.IMemento
import engine.physics.bodies.BodyEdit
import game.modifiers.logic.template.TimeModifierLogic
import game.modifiers.data.ShapeModifierData

class ShapeModifierLogic(entity: Entity) : TimeModifierLogic<ShapeModifierData>(entity) {
	private lateinit var physicsMemento: IMemento

	override fun save() {
		physicsMemento = entity.getComponent<PhysicsEntityComponent>().body.save()
	}

	override fun applyModifier(modifier: ShapeModifierData) {
		BodyEdit.setShape(entity, modifier.shape)
	}

	override fun restoreDefault() {
		//entity.getComponent(PhysicsEntityComponent::class).restore(physicsMemento)

		val bodyBuilder = entity.getComponent<DefaultBodyComponent>().value

		BodyEdit.setShape(entity, bodyBuilder.shape)
	}
}