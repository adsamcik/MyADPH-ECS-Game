package ecs.system

import ecs.component.PhysicsEntityComponent
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode

class BoundSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val canvasWidth = Core.canvas.width
		val canvasHeight = Core.canvas.height

		entities.forEach {
			val position = it.getComponent(PhysicsEntityComponent::class).body.position

			if (position.x < 0 ||
					position.x > canvasWidth ||
					position.y < 0 ||
					position.y > canvasHeight) {
				EntityManager.removeEntity(it)
			}
		}
	}

	override val requirements = ECInclusionNode(PhysicsEntityComponent::class)

}