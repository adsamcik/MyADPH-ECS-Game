package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import kotlinx.serialization.*
import kotlin.collections.set
import kotlin.reflect.KClass


//Command pattern
@Serializable(with = ModifierCommandFactorySerializer::class)
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

	@SerialName("commands")
	var commandCollection: List<IModifierFactory>
		get() = commands.values.toList()
		set(value) {
			addModifiers(value)
		}

	fun addModifier(modifier: IModifierFactory) {
		if (commands.containsKey(modifier::class))
			throw Error("ModifierUtility of type ${modifier::class.simpleName} was already added.")

		commands[modifier::class] = modifier
	}

	fun addModifiers(vararg modifiers: IModifierFactory) {
		modifiers.forEach { addModifier(it) }
	}

	fun addModifiers(modifiers: Collection<IModifierFactory>) {
		modifiers.forEach { addModifier(it) }
	}

	fun setEntity(entity: Entity) {
		this.entity = entity
	}

	fun apply(component: ModifierReceiverComponent) {
		if (entity.id < 0)
			throw Error("You need to set entity first")

		commands.forEach {
			component.addModifier(it.value.build(entity))
		}
	}
}

@Serializer(forClass = ModifierCommandFactory::class)
object ModifierCommandFactorySerializer : KSerializer<ModifierCommandFactory> {
	override fun deserialize(input: Decoder): ModifierCommandFactory {
		val factory = ModifierCommandFactory()
		val collection = input.decodeSerializableValue(ModifierSerializer.list)
		factory.addModifiers(collection)
		return factory
	}

	override val descriptor: SerialDescriptor
		get() = throw NotImplementedError()

	override fun serialize(output: Encoder, obj: ModifierCommandFactory) {
		output.encodeSerializableValue(ModifierSerializer.list, obj.commandCollection)
	}

}