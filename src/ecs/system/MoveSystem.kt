package ecs.system

import ecs.component.PositionComponent
import ecs.component.UserControlledComponent
import ecs.component.VelocityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.input.Input
import engine.system.ComponentInclusion
import engine.system.ComponentRequirement
import engine.system.ISystem

class MoveSystem : ISystem {
    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val velocityComponent = EntityManager.getComponent(it, VelocityComponent::class.js)

            positionComponent.x += velocityComponent.x * deltaTime
            positionComponent.y += velocityComponent.y * deltaTime
        }
    }


    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(VelocityComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(UserControlledComponent::class.js, ComponentInclusion.MustNotHave))
}

class UserMoveSystem : ISystem {
    override fun update(deltaTime: Double, entities: Collection<Entity>) {
        val horizontalInput = Input.horizontal()
        val verticalInput = Input.vertical()

        if (horizontalInput == 0.0 && verticalInput == 0.0)
            return

        entities.forEach {
            val positionComponent = EntityManager.getComponent(it, PositionComponent::class.js)
            val velocityComponent = EntityManager.getComponent(it, VelocityComponent::class.js)

            positionComponent.x += horizontalInput * velocityComponent.x * deltaTime
            positionComponent.y += verticalInput * velocityComponent.y * deltaTime
        }
    }


    override val requirements = listOf(
            ComponentRequirement(PositionComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(UserControlledComponent::class.js, ComponentInclusion.MustHave),
            ComponentRequirement(VelocityComponent::class.js, ComponentInclusion.MustHave))
}