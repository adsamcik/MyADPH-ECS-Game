package ecs.system

import ecs.component.GraphicsComponent
import ecs.component.PhysicsEntityComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class CircleRendererSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEntityComponent::class).andInclude(GraphicsComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsComponent = it.getComponent(PhysicsEntityComponent::class)
			val graphicsComponent = it.getComponent(GraphicsComponent::class)
			val body = physicsComponent.body

			graphicsComponent.value.apply {
				rotation = body.angle
				x = body.position.x
				y = body.position.y
			}
		}
	}
}