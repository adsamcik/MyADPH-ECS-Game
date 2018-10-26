package ecs.components.modifiers

import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Graphics
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import game.modifiers.IModifier
import game.modifiers.ITimedModifier

//Bringing logic away from this components would really hurt the access, because there needs to be some control over the elements
class ModifierReceiverComponent(val entity: Entity,
                                private val bodyBuilder: BodyBuilder) : IComponent {

	private val _modifiers = mutableMapOf<String, IModifier>()

	private val _timedModifiers = mutableMapOf<String, ITimedModifier>()

	val modifiers: Map<String, IModifier>
		get() = _modifiers

	val timedModifiers: Map<String, ITimedModifier>
		get() = _timedModifiers

	private val defaultBody = bodyBuilder.buildBody(entity)

	fun addModifier(modifier: IModifier) {
		when (modifier) {
			is ITimedModifier -> {
				if (_timedModifiers.isEmpty())
					EntityManager.addComponent(entity, ActiveTimedModifierComponent())

				if (_timedModifiers.containsKey(modifier.id))
					return

				_timedModifiers[modifier.id] = modifier
			}
			else -> {
				if (_modifiers.containsKey(modifier.id))
					return

				_modifiers[modifier.id] = modifier
			}
		}

		modifier.apply(this)
	}

	fun removeModifier(modifier: IModifier) {
		when (modifier) {
			is ITimedModifier -> {
				_timedModifiers.remove(modifier.id)
				if (_timedModifiers.isEmpty())
					EntityManager.removeComponent(entity, ActiveTimedModifierComponent::class)
			}
			else -> _modifiers.remove(modifier.id)
		}

		modifier.restore(this)
	}

	fun setRestitution(restitution: Double) {
		entity.getComponent(PhysicsEntityComponent::class).body.restitution = restitution
	}

	fun restoreRestitution() {
		setRestitution(defaultBody.restitution)
	}

	fun setBody(bodyBuilder: BodyBuilder) {
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

	fun restoreBody() {
		setBody(bodyBuilder)
	}
}