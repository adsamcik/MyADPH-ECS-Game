package ecs.system

import ecs.components.DisplayFollowComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Graphics
import engine.entity.Entity
import engine.system.ISystem
import utility.Double2
import utility.ECInclusionNode
import utility.INode
import utility.andInclude

class DisplayFollowSystem : ISystem {
	override val requirements: INode<Entity> =
		ECInclusionNode(DisplayFollowComponent::class).andInclude(PhysicsEntityComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		var avgPosition = Double2()
		entities.forEach {
			val body = it.getComponent(PhysicsEntityComponent::class).body
			avgPosition += body.position
		}

		avgPosition = (avgPosition / entities.size)

		val currentCenter = Graphics.center
		val diff = avgPosition - currentCenter

		val moveDelta = 1 - kotlin.math.exp(-deltaTime * diff.magnitude / 10.0)

		val targetPosition = Double2(
			diff.x * moveDelta + currentCenter.x,
			diff.y * moveDelta + currentCenter.y
		)

		Graphics.centerAt(targetPosition)

		//todo fix ui position
		//stage.pivot.set(targetPosition.x, targetPosition.y)


	}

}