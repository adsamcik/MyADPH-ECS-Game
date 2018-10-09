package ecs.system

import ecs.component.RotateMeComponent
import ecs.component.RotationComponent
import engine.entity.Entity
import engine.system.ComponentInclusion
import engine.system.ComponentRequirement
import engine.system.ISystem

class RoundAndRoundWeGoSystem : ISystem {
    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val rotationComponent = it.getComponent(RotationComponent::class)
            val rotateMeComponent = it.getComponent(RotateMeComponent::class)

            rotationComponent.rotation += rotateMeComponent.speed * deltaTime
        }
    }

    override val requirements = listOf(
            ComponentRequirement(RotationComponent::class, ComponentInclusion.MustHave),
            ComponentRequirement(RotateMeComponent::class, ComponentInclusion.MustHave))
}