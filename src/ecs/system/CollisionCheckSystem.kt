package ecs.system

import ecs.component.DynamicColliderComponent
import ecs.component.IColliderComponent
import ecs.component.PositionComponent
import ecs.component.StaticColliderComponent
import engine.entity.Entity
import engine.system.EntityComponentCollection
import engine.system.IComponentSystem
import extensions.forEachSince
import utility.ECInclusionNode
import utility.INode
import utility.andInclude
import utility.orInclude

class CollisionCheckSystem : IComponentSystem {
	private fun checkCollision(firstCollider: IColliderComponent, secondCollider: IColliderComponent) {
		val collisionPoints = firstCollider.shape.collidesWith(secondCollider.shape)
		if (collisionPoints.isNotEmpty()) {
			val data = collisionPoints.first()
			firstCollider.collisionData = data
			secondCollider.collisionData = data
		}

	}

	override fun update(deltaTime: Double, entities: Collection<Entity>, components: EntityComponentCollection) {
		val staticColliders = components[StaticColliderComponent::class].unsafeCast<List<StaticColliderComponent>?>()
		val colliders = components[DynamicColliderComponent::class].unsafeCast<List<DynamicColliderComponent>?>()
				?: return

		colliders.forEachIndexed { index, component ->
			run {
				colliders.forEachSince(index + 1) { checkCollision(component, it) }
				staticColliders?.forEach { checkCollision(component, it) }
			}
		}

	}


	override val requirements: INode<Entity> = ECInclusionNode(DynamicColliderComponent::class).orInclude(StaticColliderComponent::class).andInclude(PositionComponent::class)
}
