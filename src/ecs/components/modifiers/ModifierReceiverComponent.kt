package ecs.components.modifiers

import Matter
import ecs.components.GraphicsComponent
import ecs.components.InitializePhysicsComponent
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

	//todo maybe hide this, so it can't be accessed directly
	val modifiers = mutableListOf<IModifier>()

	val timedModifiers = mutableListOf<ITimedModifier>()

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
				if (timedModifiers.isEmpty())
					EntityManager.addComponent(entity, ActiveTimedModifierComponent())
				timedModifiers.add(modifier)
			}
			else -> modifiers.add(modifier)
		}

		modifier.apply(this)
	}

	fun removeModifier(modifier: IModifier) {
		when (modifier) {
			is ITimedModifier -> {
				timedModifiers.remove(modifier)
				if (timedModifiers.isEmpty())
					EntityManager.removeComponent(entity, ActiveTimedModifierComponent::class)
			}
			else -> modifiers.remove(modifier)
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

		val oldBody = entity.getComponent(PhysicsEntityComponent::class).body
		Matter.World.remove(PhysicsEngine.world, oldBody)

		Matter.Body.setPosition(body, oldBody.position)
		Matter.Body.setAngle(body, oldBody.angle)

		EntityManager.setComponent(entity, PhysicsEntityComponent(body))
		Matter.World.add(PhysicsEngine.world, body)
		body.entity = entity

		Graphics.dynamicContainer.addChild(graphics)
		EntityManager.setComponents(entity, GraphicsComponent(graphics))
		EntityManager.removeComponent(entity, PhysicsEntityComponent::class)
		EntityManager.addComponent(entity, InitializePhysicsComponent(PhysicsEngine.world, body))
	}

	fun restoreBody() {
		console.log(entity)
		setBody(bodyBuilder)
	}
}