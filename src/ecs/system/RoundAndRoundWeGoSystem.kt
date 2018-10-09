package ecs.system

import ecs.component.RotateMeComponent
import ecs.component.RotationComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ComponentRequirement
import engine.system.ISystem

class RoundAndRoundWeGoSystem : ISystem {
    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val rotationComponent = EntityManager.getComponent(it, RotationComponent::class.js)
            val rotateMeComponent = EntityManager.getComponent(it, RotateMeComponent::class.js)

            rotationComponent.rotation += rotateMeComponent.speed * deltaTime
        }
    }

    override val requirements = listOf(
            ComponentRequirement(RotationComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave),
            ComponentRequirement(RotateMeComponent::class.js, ComponentRequirement.ComponentInclusion.MustHave))
}