package ecs.components.modifiers

import Matter
import ecs.components.GraphicsComponent
import ecs.components.PhysicsEntityComponent
import engine.Graphics
import engine.PhysicsEngine
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

	private val restitution: Number
	private val density: Number

	init {
		val physicsComponent = entity.getComponent(PhysicsEntityComponent::class)
		restitution = physicsComponent.body.restitution
		density = physicsComponent.body.density

		if (physicsComponent.body.isStatic)
			throw Error("Modifier components cannot be used on static objects")
	}

	fun addModifier(modifier: IModifier) {
		when (modifier) {
			is ITimedModifier -> {
				if (_timedModifiers.isEmpty())
					EntityManager.addComponent(entity, ActiveTimedModifierComponent())

				if(_timedModifiers.containsKey(modifier.id))
						return

				_timedModifiers[modifier.id] = modifier
			}
			else -> {
				if(_modifiers.containsKey(modifier.id))
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

	fun setRestitution(restitution: Number) {
		entity.getComponent(PhysicsEntityComponent::class).body.restitution = restitution
	}

	fun restoreRestitution() {
		setRestitution(restitution)
	}

	fun setDensity(density: Number) {
		Matter.Body.setDensity(entity.getComponent(PhysicsEntityComponent::class).body, density)
	}

	fun restoreDensity() {
		setDensity(density)
	}

	fun setBody(bodyBuilder: BodyBuilder) {
		val (body, graphics) = bodyBuilder.build()
		entity.getComponent(GraphicsComponent::class).cleanup()

		val oldPhysics = entity.getComponent(PhysicsEntityComponent::class)

		Matter.Body.setPosition(body, oldPhysics.body.position)
		Matter.Body.setAngle(body, oldPhysics.body.angle)
		Matter.Body.setVelocity(body, oldPhysics.body.velocity)
		oldPhysics.cleanup()

		EntityManager.setComponent(entity, PhysicsEntityComponent(body))
		Matter.World.add(PhysicsEngine.world, body)
		body.entity = entity

		Graphics.dynamicContainer.addChild(graphics)
		EntityManager.setComponents(entity, GraphicsComponent(graphics))
		//EntityManager.addComponent(entity, InitializePhysicsComponent(PhysicsEngine.world, body))
	}

	fun restoreBody() {
		setBody(bodyBuilder)
	}
}