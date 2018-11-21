package game.checkpoints

import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointType
import general.Double2

class CheckpointManager {
	private var nextId: Int = 0
	private var isClosed = false

	fun createCheckpoint(position: Double2, checkpointType: CheckpointType): CheckpointComponent {
		if (isClosed)
			throw IllegalStateException("Cannot create new checkpoint after creating end checkpoint")

		if (nextId == 0) {
			if (checkpointType != CheckpointType.Start)
				throw IllegalStateException("First checkpoint must be start")
		} else if (checkpointType == CheckpointType.Start)
			throw IllegalStateException("Only first checkpoint can be start")

		return CheckpointComponent(nextId++, position, checkpointType)
	}
}