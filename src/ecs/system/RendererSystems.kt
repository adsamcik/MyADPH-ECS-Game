package ecs.system

import ecs.component.PhysicsEntityComponent
import ecs.component.PixiCircleComponent
import engine.entity.Entity
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class CircleRendererSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PhysicsEntityComponent::class).andInclude(PixiCircleComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val physicsComponent = it.getComponent(PhysicsEntityComponent::class)
			val pixiCircleComponent = it.getComponent(PixiCircleComponent::class)
			val body = physicsComponent.body

			pixiCircleComponent.value.x = body.position.x
			pixiCircleComponent.value.y = body.position.y
		}
	}
}