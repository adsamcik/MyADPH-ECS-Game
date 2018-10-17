package ecs.components.modifiers

import Matter
import ecs.components.GraphicsComponent
import ecs.components.PhysicsEntityComponent
import engine.Graphics
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import game.IModifier
import game.ITimedModifier

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
			is ITimedModifier -> timedModifiers.add(modifier)
			else -> modifiers.add(modifier)
		}

		modifier.apply(this)
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
		//Matter.World.remove(PhysicsEngine.world, entity.getComponent(PhysicsEntityComponent::class).body)
		val oldBody = entity.getComponent(PhysicsEntityComponent::class).body

		oldBody.torque = 0
		oldBody.angle = 0
		oldBody

		Matter.Body.setVertices(oldBody, body.vertices)




		Graphics.dynamicContainer.addChild(graphics)
		EntityManager.setComponents(entity, GraphicsComponent(graphics))
		//EntityManager.removeComponent(entity, PhysicsEntityComponent::class)
		//EntityManager.addComponent(entity, InitializePhysicsComponent(PhysicsEngine.world, body))
	}

	fun restoreBody() {
		setBody(bodyBuilder)
	}
}