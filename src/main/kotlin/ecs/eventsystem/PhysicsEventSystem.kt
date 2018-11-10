package ecs.eventsystem

import ecs.components.modifiers.ModifierSpreaderComponent
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.events.PhysicsCollisionEvent
import engine.physics.events.PhysicsEventManager
import engine.physics.events.PhysicsEventType
import engine.system.IEventSystem
import kotlin.reflect.KClass

abstract class PhysicsEventSystem<T, U>(
	protected val physicsEventManager: PhysicsEventManager,
	private val componentAType: KClass<T>,
	private val componentBType: KClass<U>
) : IEventSystem where T : IComponent, U : IComponent {

	protected fun subscribeOnCollisionStart() {
		physicsEventManager.subscribe(PhysicsEventType.CollisionStart, this::onCollision)
	}

	protected fun subscribeOnCollisionEnd() {
		physicsEventManager.subscribe(PhysicsEventType.CollisionEnd, this::onCollision)
	}

	protected fun unsubscribeOnCollisionStart() {
		physicsEventManager.unsubscribe(PhysicsEventType.CollisionStart, this::onCollision)
	}

	protected fun unsubscribeOnCollisionEnd() {
		physicsEventManager.unsubscribe(PhysicsEventType.CollisionEnd, this::onCollision)
	}

	private fun onCollision(collisionEvent: PhysicsCollisionEvent) {
		val entityA = collisionEvent.entityA
		val entityB = collisionEvent.entityB

		if (hasComponentA(entityA)) {
			if (hasComponentB(entityB))
				onTriggered(collisionEvent.type, entityA, entityB)
		} else if (hasComponentA(entityB)) {
			if (hasComponentB(entityA))
				onTriggered(collisionEvent.type, entityB, entityA)
		}
	}

	private fun hasComponentA(entity: Entity) = EntityManager.hasComponent(entity, componentAType)
	private fun hasComponentB(entity: Entity) = EntityManager.hasComponent(entity, componentBType)

	private fun hasSpreader(entity: Entity): Boolean =
		EntityManager.hasComponent(entity, ModifierSpreaderComponent::class)

	private fun onTriggered(eventType: PhysicsEventType, entityA: Entity, entityB: Entity) {
		val componentA = EntityManager.getComponent(entityA, componentAType)
		val componentB = EntityManager.getComponent(entityB, componentBType)
		onTriggered(eventType, entityA, componentA, entityB, componentB)
	}

	abstract fun onTriggered(
		event: PhysicsEventType,
		entityA: Entity,
		componentA: T,
		entityB: Entity,
		componentB: U
	)
}