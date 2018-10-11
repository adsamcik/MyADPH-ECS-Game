package ecs.system

import ecs.component.DynamicColliderComponent
import ecs.component.PhysicsComponent
import ecs.component.VelocityComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude
import kotlin.math.sign

class PhysicsSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val velocity = it.getComponent(VelocityComponent::class)
			val physics = it.getComponent(PhysicsComponent::class)

			if (physics.affectedByGravity)
				velocity.y += GRAVITY_ACCELERATION * deltaTime

			val deltaFriction = physics.friction * deltaTime
			val xSign = velocity.x.sign
			val ySign = velocity.y.sign

			velocity.y -= deltaFriction * ySign
			velocity.x -= deltaFriction * xSign

			if (velocity.x.sign != xSign)
				velocity.x = 0.0

			if (velocity.y.sign != ySign)
				velocity.y = 0.0
		}
	}

	override val requirements = ECInclusionNode(VelocityComponent::class).andInclude(PhysicsComponent::class)


	companion object {
		const val GRAVITY_ACCELERATION = 9.80665
	}
}

class CollisionPhysicsSystem : ISystem {
	override val requirements = ECInclusionNode(VelocityComponent::class).andInclude(DynamicColliderComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val collider = it.getComponent(DynamicColliderComponent::class)

			val collision = collider.collisionData
			if (collision != null) {
				val velocity = it.getComponent(VelocityComponent::class)
				val bounceDirection = velocity.vector - (collision.normal * 2.0 * velocity.vector.dot(collision.normal))

				val otherCollider = if (collision.firstCollider == collider.shape) collision.secondCollider else collision.firstCollider
				val

				velocity.vector.x = bounceDirection.x
				velocity.vector.y = bounceDirection.y
			}
		}
	}

}