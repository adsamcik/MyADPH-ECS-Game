package ecs.system

import Matter
import ecs.component.InitializePhysicsComponent
import ecs.component.PhysicsEntityComponent
import ecs.component.PositionComponent
import ecs.component.RenderStyleComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class PhysicsInitializationSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(InitializePhysicsComponent::class).andInclude(PositionComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val positionComponent = it.getComponent(PositionComponent::class)
			val initPhysics = it.getComponent(InitializePhysicsComponent::class)

			val body = initPhysics.shape.buildBody(positionComponent.value)

			body.render = initPhysics.render
			Matter.Body.setStatic(body, initPhysics.static)


			EntityManager.addComponents(it, PhysicsEntityComponent(body), RenderStyleComponent())

			Matter.World.add(initPhysics.world, body)
			EntityManager.removeComponent(it, InitializePhysicsComponent::class)
		}
	}
}