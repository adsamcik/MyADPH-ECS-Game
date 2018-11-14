package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent

object ModifierUtility {
	fun add(receiverComponent: ModifierReceiverComponent, modifierData: IModifierData) {
		var logic = receiverComponent.modifierLogicList[modifierData::class]
		if (logic == null) {
			logic = modifierData.createNewLogicFor(receiverComponent.entity)
			receiverComponent.modifierLogicList[modifierData::class] = logic
		}

		logic.setModifier(modifierData)
	}

	fun remove(receiverComponent: ModifierReceiverComponent, modifierData: IModifierData) {
		val logic = receiverComponent.modifierLogicList[modifierData::class]
			?: throw Error("Receiver has no logic component of type ${modifierData::class.simpleName}")

		logic.removeModifier(modifierData)
	}
}