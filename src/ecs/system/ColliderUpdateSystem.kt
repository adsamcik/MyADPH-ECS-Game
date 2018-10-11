package ecs.system

import ecs.component.DynamicColliderComponent
import ecs.component.PositionComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class DynamicColliderUpdateSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(DynamicColliderComponent::class).andInclude(PositionComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val collider = it.getComponent(DynamicColliderComponent::class)
			val position = it.getComponent(PositionComponent::class)
			collider.shape.position.x = position.x
			collider.shape.position.y = position.y
		}
	}

}