package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent

object ModifierUtility {
	fun add(receiverComponent: ModifierReceiverComponent, modifier: IModifier) {
		var logic = receiverComponent.modifierLogicList[modifier::class]
		if (logic == null) {
			logic = modifier.createNewLogicFor(receiverComponent.entity)
			receiverComponent.modifierLogicList[modifier::class] = logic
		}

		logic.setModifier(modifier)
	}

	fun remove(receiverComponent: ModifierReceiverComponent, modifier: IModifier) {
		val logic = receiverComponent.modifierLogicList[modifier::class]
			?: throw Error("Receiver has no logic component of type ${modifier::class.simpleName}")

		logic.removeModifier(modifier)
	}
}