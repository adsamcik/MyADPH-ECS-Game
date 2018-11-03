package ecs.system

import ecs.components.GraphicsComponent
import ecs.components.modifiers.ActiveModifierComponent
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import engine.physics.bodies.IBody
import engine.system.ISystem
import game.modifiers.IModifier
import game.modifiers.IModifierLogic
import utility.ECInclusionNode
import utility.INode
import kotlin.reflect.KClass

class ModifierUpdateSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(ModifierReceiverComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach { entity ->
			val modifierComponent = entity.getComponent(ModifierReceiverComponent::class)
			modifierComponent.modifierLogics.forEach { (type, logic) ->
				logic.update(deltaTime)
				checkModifiers(entity, type, logic, modifierComponent)
			}
		}
	}

	private fun checkModifiers(
		entity: Entity,
		type: KClass<out IModifier>,
		logic: IModifierLogic,
		modifierReceiverComponent: ModifierReceiverComponent
	) {
		if (logic.hasNoModifiers) {
			modifierReceiverComponent.modifierLogics.remove(type)

			if (modifierReceiverComponent.modifierLogics.isEmpty())
				EntityManager.removeComponent(entity, ActiveModifierComponent::class)
		}
	}


	private fun setBody(entity: Entity, bodyBuilder: BodyBuilder) {
		val body = bodyBuilder.buildBody(entity)
		val graphics = bodyBuilder.buildGraphics()
		entity.getComponent(GraphicsComponent::class).cleanup()

		val oldPhysics = entity.getComponent(PhysicsEntityComponent::class)

		body.position = oldPhysics.body.position
		body.angle = oldPhysics.body.angle
		body.velocity = oldPhysics.body.velocity
		oldPhysics.cleanup()

		EntityManager.setComponent(entity, PhysicsEntityComponent(body))
		body.entity = entity

		Graphics.dynamicContainer.addChild(graphics)
		EntityManager.setComponents(entity, GraphicsComponent(graphics))
		//EntityManager.addComponent(entity, PhysicsInitializationComponent(PhysicsEngine.world, body))
	}

	private fun restoreBody() {
		setBody(bodyBuilder)
	}
}