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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val requirements: Collection<ComponentRequirement> = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(VelocityComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(PhysicsComponent::class.js, ComponentInclusion.MustHave))

}