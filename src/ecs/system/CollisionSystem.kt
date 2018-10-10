package ecs.system

import ecs.component.ColliderComponent
import ecs.component.PositionComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECExclusionNode
import utility.INode
import utility.andInclude

class CollisionSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override val requirements: INode<Entity> = ECExclusionNode(PositionComponent::class).andInclude(ColliderComponent::class)
}
