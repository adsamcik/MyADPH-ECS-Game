package ecs.system

import ecs.component.PhysicsEntityComponent
import ecs.component.PixiGraphicsComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class CircleRendererSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEntityComponent::class).andInclude(PixiGraphicsComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsComponent = it.getComponent(PhysicsEntityComponent::class)
			val pixiCircleComponent = it.getComponent(PixiGraphicsComponent::class)
			val body = physicsComponent.body

			pixiCircleComponent.value.apply {
				rotation = body.angle
				x = body.position.x
				y = body.position.y
			}
		}
	}
}