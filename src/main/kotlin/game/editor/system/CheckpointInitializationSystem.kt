package game.editor.system

import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointType
import engine.entity.Entity
import engine.entity.EntityManager
import engine.system.ISystem
import engine.system.requirements.ECInclusionNode
import engine.system.requirements.INode
import game.editor.component.CheckpointDefinitionComponent

class CheckpointInitializationSystem : ISystem {
	override val requirements: INode<Entity>
		get() = ECInclusionNode(CheckpointDefinitionComponent::class)

	override fun update(deltaTime: Double, entities: Collection<Entity>) {
		val components = entities.map { it to EntityManager.getComponent<CheckpointDefinitionComponent>(it) }

		require(components.size >= 2) { "Level needs to have at least two checkpoints!" }

		val sortedComponents = components.sortedBy { it.second.orderNumber }

		sortedComponents.first().run {
			initializeComponent(first, 0, second, CheckpointType.Start)
		}

		for (i in 1 until sortedComponents.size - 1) {
			sortedComponents[i].run {
				initializeComponent(first, i, second, CheckpointType.Standard)
			}
		}

		sortedComponents.last().run {
			initializeComponent(first, sortedComponents.size - 1, second, CheckpointType.End)
		}
	}

	private fun initializeComponent(
		entity: Entity,
		id: Int,
		definitionComponent: CheckpointDefinitionComponent,
		type: CheckpointType
	) {
		EntityManager.addComponent(entity, CheckpointComponent(id, definitionComponent.respawnPosition, type))
		EntityManager.removeComponent(entity, definitionComponent)
	}
}