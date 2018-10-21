package ecs.system

import ecs.components.PhysicsDynamicEntityComponent
import ecs.components.PhysicsEntityComponent
import engine.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode
import utility.andInclude

class BoundSystem : ISystem {
	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val canvasWidth = Graphics.pixi.screen.width
		val canvasHeight = Graphics.pixi.screen.height

		entities.forEach {
			val position = it.getComponent(PhysicsEntityComponent::class).body.position

			if (position.x < -10 ||
					position.x > canvasWidth ||
					position.y < -10 ||
					position.y > canvasHeight) {
				EntityManager.removeEntity(it)
			}
		}
	}

	override val requirements = ECInclusionNode(PhysicsEntityComponent::class).andInclude(PhysicsDynamicEntityComponent::class)

}