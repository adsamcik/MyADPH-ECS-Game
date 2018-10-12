package ecs.system

import ecs.component.BoundsComponent
import ecs.component.PositionComponent
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude

class BoundSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val canvasWidth = Core.canvas.width
		val canvasHeight = Core.canvas.height

		entities.forEach {
			val positionComponent = it.getComponent(PositionComponent::class)
			val bounds = it.getComponent(BoundsComponent::class).value

			if (positionComponent.x + bounds.rightOffset < 0 ||
					positionComponent.x + bounds.leftOffset > canvasWidth ||
					positionComponent.y + bounds.bottomOffset < 0 ||
					positionComponent.y + bounds.topOffset > canvasHeight) {
				EntityManager.removeEntity(it)
			}
		}
	}

	override val requirements = ECInclusionNode(PositionComponent::class).andInclude(BoundsComponent::class)

}