package ecs.system

import ecs.components.InitializePhysicsComponent
import ecs.components.PhysicsEntityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import jslib.Matter
import utility.ECInclusionNode
import utility.INode

//todo get rid of this
class PhysicsInitializationSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(InitializePhysicsComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val initPhysics = it.getComponent(InitializePhysicsComponent::class)
			val physicsObject = initPhysics.body

			physicsObject.entity = it

			EntityManager.addComponent(it, PhysicsEntityComponent(physicsObject))

			Matter.World.add(initPhysics.world, physicsObject)
			EntityManager.removeComponent(it, InitializePhysicsComponent::class)
		}
	}
}