package ecs.system

import ecs.components.physics.PhysicsWorldComponent
import engine.entity.Entity
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode

class PhysicsUpdateSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(PhysicsWorldComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			it.getComponent<PhysicsWorldComponent>().engine.update(deltaTime)
		}
	}

}