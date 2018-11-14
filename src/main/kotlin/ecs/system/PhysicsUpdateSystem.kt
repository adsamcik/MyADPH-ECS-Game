package ecs.system

import ecs.components.physics.PhysicsUpdateComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode

class PhysicsUpdateSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PhysicsUpdateComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			it.getComponent<PhysicsUpdateComponent>().engine.update(deltaTime)
		}
	}

}