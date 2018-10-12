package ecs.system

import Matter
import ecs.component.InitializePhysicsComponent
import ecs.component.PhysicsEntityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode

class PhysicsInitializationSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(InitializePhysicsComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val initPhysics = it.getComponent(InitializePhysicsComponent::class)
			val physicsObject = initPhysics.body

			EntityManager.addComponent(it, PhysicsEntityComponent(physicsObject))

			Matter.World.add(initPhysics.world, physicsObject)
			EntityManager.removeComponent(it, InitializePhysicsComponent::class)
		}
	}
}