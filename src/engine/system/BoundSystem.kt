package engine.system

import ecs.component.PositionComponent
import ecs.component.VelocityComponent
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import utility.ECInclusionNode
import utility.andInclude

class BoundSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val canvasWidth = Core.canvas.width
		val canvasHeight = Core.canvas.height

		entities.forEach {
			val positionComponent = it.getComponent(PositionComponent::class)

			if (positionComponent.x < 0 || positionComponent.x > canvasWidth || positionComponent.y < 0 || positionComponent.y > canvasHeight) {
				EntityManager.removeEntity(it)
			}
		}
	}

	override val requirements = ECInclusionNode(VelocityComponent::class).andInclude(PositionComponent::class)

}