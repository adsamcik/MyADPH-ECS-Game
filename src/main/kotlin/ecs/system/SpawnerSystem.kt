package ecs.system

import ecs.components.SpawnerComponent
import engine.entity.Entity
import engine.system.ISystem
import game.levels.EntityCreator
import utility.ECInclusionNode
import utility.INode

class SpawnerSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(SpawnerComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		entities.forEach {
			val spawner = it.getComponent<SpawnerComponent>()


			spawner.time += deltaTime

			if(spawner.time >= spawner.spawnDeltaTime) {
				spawner.time = spawner.time.rem(spawner.spawnDeltaTime)

				EntityCreator.createWithBody {
					setBodyBuilder(spawner.bodyBuilder)
					spawner.spawnFunction.invoke(this, spawner.bodyBuilder)
				}
			}
		}
	}

}