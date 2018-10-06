package ecs.system

import ecs.component.PositionComponent
import ecs.component.VelocityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ComponentRequirement
import engine.system.ISystem

class MoveSystem : ISystem{
    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val velocityComponent = EntityManager.getComponent(it, VelocityComponent::class.js)

            positionComponent.x += velocityComponent.x * deltaTime
            positionComponent.y += velocityComponent.y * deltaTime
        }
    }



    override fun componentSpecification(): Collection<ComponentRequirement> = requirement

    companion object {
        val requirement = listOf(
                ComponentRequirement(PositionComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave),
                ComponentRequirement(VelocityComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave))
    }
}