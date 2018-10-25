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

		avgPosition /= entities.size

		val stage = Graphics.pixi.stage
		val renderer = Graphics.pixi.renderer

		val diff = avgPosition - stage.pivot

		val moveDelta = 1 - kotlin.math.exp(-deltaTime * diff.magnitude / 10.0)

		val targetPosition = Double2(
			diff.x * moveDelta + stage.pivot.x.toDouble(),
			diff.y * moveDelta + stage.pivot.y.toDouble()
		)

		//todo fix ui position
		stage.pivot.set(targetPosition.x, targetPosition.y)
		stage.position.set(
			renderer.width.toDouble() / 2.0 * stage.scale.x.toDouble(),
			renderer.height.toDouble() / 2.0 * stage.scale.y.toDouble()
		)
	}

}