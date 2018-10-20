package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import kotlin.reflect.KClass


//Command pattern
class ModifierCommandFactory {
	val isEmpty
		get() = commands.isEmpty()

	val isNotEmpty
		get() = commands.isNotEmpty()

	private val commands = mutableMapOf<KClass<out IModifierFactory>, IModifierFactory>()

	fun addModifier(modifier: IModifierFactory) {
		if (commands.containsKey(modifier::class))
			throw Error("Modifier of type ${modifier::class.simpleName} was already added.")

		commands[modifier::class] = modifier
	}

	fun addModifiers(vararg modifiers: IModifierFactory) {
		modifiers.forEach { addModifier(it) }
	}

	fun setEntity(entity: Entity) {
		commands.forEach { it.value.setEntity(entity) }
	}

	fun apply(component: ModifierReceiverComponent) {
		console.log("Apply")
		commands.forEach {
			component.addModifier(it.value.build())
		}
	}
}