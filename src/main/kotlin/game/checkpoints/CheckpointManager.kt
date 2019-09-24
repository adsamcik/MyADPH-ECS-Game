package game.checkpoints

import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointType
import general.Double2

class CheckpointManager {
	private var nextId: Int = 0
	private var isClosed = false

	fun createCheckpoint(position: Double2, checkpointType: CheckpointType): CheckpointComponent {
		check(!isClosed) { "Cannot create new checkpoint after creating end checkpoint" }

		if (nextId == 0) {
			check(checkpointType == CheckpointType.Start) { "First checkpoint must be start" }
		} else {
			check(checkpointType != CheckpointType.Start) { "Only first checkpoint can be start" }
		}

		return CheckpointComponent(nextId++, position, checkpointType)
	}
}