package ecs.system

import ecs.component.PhysicsComponent
import ecs.component.PositionComponent
import ecs.component.VelocityComponent
import engine.entity.Entity
import engine.system.ComponentInclusion
import engine.system.ComponentRequirement
import engine.system.ISystem

class PhysicsSystem : ISystem {
    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val velocity = it.getComponent(PositionComponent::class)
        }
    }

    override val requirements: Collection<ComponentRequirement> = listOf(
            ComponentRequirement(PositionComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(VelocityComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(PhysicsComponent::class, ComponentInclusion.MustHave))


    companion object {
        const val GRAVITY_ACCELERATION = 9.80665
    }
}