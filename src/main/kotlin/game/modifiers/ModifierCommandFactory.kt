package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KClass


//Command pattern
@Serializable
class ModifierCommandFactory {
	@Transient
	var entity: Entity = Entity(-1)

	@Transient
	val isEmpty
		get() = commands.isEmpty()

	@Transient
	val isNotEmpty
		get() = commands.isNotEmpty()

	@Transient
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
		this.entity = entity
	}

	fun apply(component: ModifierReceiverComponent) {
		if(entity.id < 0)
			throw Error("You need to set entity first")

		commands.forEach {
			component.addModifier(it.value.build(entity))
		}
	}
}