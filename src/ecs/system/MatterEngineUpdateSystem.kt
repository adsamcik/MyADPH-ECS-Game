package ecs.system

import Matter
import ecs.component.PhysicsEngineComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode


class MatterEngineUpdateSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEngineComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val engineComponent = it.getComponent(PhysicsEngineComponent::class)
			Matter.Engine.update(engineComponent.engine, deltaTime)

		}
	}

}