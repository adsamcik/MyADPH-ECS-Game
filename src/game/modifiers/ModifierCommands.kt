package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import kotlin.reflect.KClass


//Command pattern
class ModifierCommandFactory {
	private val commands = mutableMapOf<KClass<out IModifierFactory>, IModifierFactory>()

	fun addModifier(modifier: IModifierFactory): ModifierCommandFactory {
		if (commands.containsKey(modifier::class))
			throw Error("Modifier of type ${modifier::class.simpleName} was already added.")

		commands[modifier::class] = modifier
		return this
	}

	fun addModifiers(vararg modifiers: IModifierFactory): ModifierCommandFactory {
		modifiers.forEach { addModifier(it) }
		return this
	}

	fun apply(component: ModifierReceiverComponent) {
		commands.forEach {
			component.addModifier(it.value.build())
		}
	}
}