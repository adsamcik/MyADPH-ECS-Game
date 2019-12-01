package game.modifiers

import ecs.components.modifiers.ModifierReceiverComponent
import engine.entity.Entity
import game.modifiers.factory.template.IModifierFactory
import kotlinx.serialization.*
import kotlin.collections.set
import kotlin.reflect.KClass


//PATTERN Command
@Serializable(with = ModifierCommandFactorySerializer::class)
class ModifierCommandFactory {
	@Transient
	var sourceEntity: Entity = Entity(-1)

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

	fun setSourceEntity(entity: Entity) {
		this.sourceEntity = entity
	}

	fun apply(component: ModifierReceiverComponent) {
		if (sourceEntity.id < 0)
			throw Error("You need to set entity first")

		commands.forEach {
			ModifierUtility.add(component, it.value.build(sourceEntity))
		}
	}
}

@Serializer(forClass = ModifierCommandFactory::class)
object ModifierCommandFactorySerializer : KSerializer<ModifierCommandFactory> {
	private val serializer get()  = PolymorphicSerializer(IModifierFactory::class)

	override fun deserialize(decoder: Decoder): ModifierCommandFactory {
		val factory = ModifierCommandFactory()
		val collection = decoder.decodeSerializableValue(serializer.list)
		factory.addModifiers(collection)
		return factory
	}

	override val descriptor: SerialDescriptor
		get() = throw NotImplementedError()

	override fun serialize(encoder: Encoder, obj: ModifierCommandFactory) {
		encoder.encodeSerializableValue(serializer.list, obj.commandCollection)
	}

}