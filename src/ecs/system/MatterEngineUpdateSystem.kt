package ecs.system

import Matter
import ecs.components.PhysicsEngineComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode


class MatterEngineUpdateSystem : ISystem {
	var lastDeltaInMs = 1.0

	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEngineComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val deltaInMilliseconds = deltaTime * 1000
		val correction = deltaInMilliseconds / lastDeltaInMs
		lastDeltaInMs = deltaInMilliseconds

		entities.forEach {
			val engineComponent = it.getComponent(PhysicsEngineComponent::class)
			Matter.Engine.update(engineComponent.value, deltaInMilliseconds)
		}
	}

}