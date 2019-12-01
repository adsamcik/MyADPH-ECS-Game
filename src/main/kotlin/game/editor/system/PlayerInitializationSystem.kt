package game.editor.system

import ecs.components.AccelerationComponent
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointMemoryComponent
import ecs.components.triggers.CheckpointType
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import game.editor.component.PlayerDefinitionComponent
import general.Double2

class PlayerInitializationSystem : ISystem {
	override val requirements: INode<Entity> = ECInclusionNode(PlayerDefinitionComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val checkpointEntityList = EntityManager.getEntityListByComponent(CheckpointComponent::class)
		val checkpointList = checkpointEntityList
			.map { it.getComponent<CheckpointComponent>() }

		require(checkpointList.size >= 2) { "Level needs to have at least two checkpoints!" }

		val first = requireNotNull(checkpointList.find { it.type == CheckpointType.Start })
		val count = checkpointList.count()

		entities.forEach { entity ->
			EntityManager.apply {
				addComponents(
					entity,
					CheckpointMemoryComponent(first, count),
					ModifierReceiverComponent(entity)
				)
				removeComponent(entity, PlayerDefinitionComponent::class)
			}
		}
	}

}