package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent

object ModifierUtility {
	fun add(receiverComponent: ModifierReceiverComponent, modifier: IModifier) {
		var logic = receiverComponent.modifierLogics[modifier::class]
		if (logic == null)
			logic = modifier.createNewLogic()

		logic.setModifier(modifier)
	}
}