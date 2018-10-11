package ecs.component

import engine.component.IComponent
import engine.physics.IShape

interface IColliderComponent : IComponent {
	val shape: IShape
}

data class ColliderComponent(override val shape: IShape) : IColliderComponent

data class StaticColliderComponent(override val shape: IShape) : IColliderComponent

