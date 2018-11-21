package engine.physics

import ecs.components.physics.PhysicsUpdateComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.engines.NullPhysicsEngine
import engine.physics.engines.PhysicsEngine

object Physics {
	var engine: PhysicsEngine = NullPhysicsEngine()
		set(value) {
			field = value

			val engineEntity = engineEntity
			if(engineEntity != null)
				EntityManager.removeEntity(engineEntity)

			this.engineEntity = EntityManager.createEntity {
				addComponent(PhysicsUpdateComponent(engine))
			}
		}

	private var engineEntity: Entity? = null

}