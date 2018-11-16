package ecs.system

import ecs.components.LifeTimeComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode

class LifeTimeSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(LifeTimeComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val lifeTimeComponent = it.getComponent<LifeTimeComponent>()
			lifeTimeComponent.timeLeft -= deltaTime
			if (lifeTimeComponent.timeLeft <= 0.0)
				EntityManager.removeEntity(it)
		}
	}

}