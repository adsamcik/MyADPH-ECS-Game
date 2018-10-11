package ecs.component

import engine.component.IComponent
import engine.physics.ColliderShape
import engine.physics.CollisionData

interface IColliderComponent : IComponent {
	val shape: ColliderShape
	var collisionData: CollisionData?
}

data class DynamicColliderComponent(override val shape: ColliderShape) : IColliderComponent {
	override var collisionData: CollisionData? = null
}

data class StaticColliderComponent(override val shape: ColliderShape) : IColliderComponent {
	override var collisionData: CollisionData? = null
}

