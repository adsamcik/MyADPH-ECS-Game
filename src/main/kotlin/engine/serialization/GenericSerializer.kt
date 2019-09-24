package engine.serialization

import kotlinx.serialization.*

abstract class GenericSerializer<T>(val name: String) : KSerializer<T> {
	override val descriptor: SerialDescriptor = StructureDescriptor(name)

	protected inline fun <reified Y : T> serialize(output: Encoder, obj: Y, serializer: KSerializer<Y>) {
		val structure = output.beginStructure(descriptor, serializer)
		structure.encodeStringElement(descriptor, 0, Y::class.simpleName!!)
		structure.encodeSerializableElement(descriptor, 1, serializer, obj)
		structure.endStructure(descriptor)
	}

	abstract fun deserialize(type: String, structure: CompositeDecoder): T

	override fun deserialize(decoder: Decoder): T {
		val structure = decoder.beginStructure(descriptor)
		var type: String? = null
		loop@ while (true) {
			when (val index = structure.decodeElementIndex(descriptor)) {
				CompositeDecoder.READ_DONE -> break@loop
				CompositeDecoder.UNKNOWN_NAME -> throw Error("Unknown name")
				StructureDescriptor.DATA_INDEX -> {
					if (type == null)
						throw Error("Type is null")

					val obj = deserialize(type, structure)
					structure.endStructure(descriptor)
					return obj
				}
				StructureDescriptor.TYPE_INDEX -> {
					type = structure.decodeStringElement(descriptor, index)
				}
			}
		}

		throw SerializationException("Invalid structure")
	}

	class StructureDescriptor(override val name: String) : SerialDescriptor {
		override val kind: SerialKind
			get() = StructureKind.CLASS

		override fun getElementIndex(name: String): Int {
			return when (name) {
				TYPE -> TYPE_INDEX
				DATA -> DATA_INDEX
				else -> throw Error("Name $name not found")
			}
		}

		override fun getElementName(index: Int): String {
			return when (index) {
				TYPE_INDEX -> TYPE
				DATA_INDEX -> DATA
				else -> throw Error("Index must be 0 or 1, but was $index")
			}
		}

		override fun isElementOptional(index: Int) = false

		companion object {
			const val TYPE = "type"
			const val DATA = "data"
			const val TYPE_INDEX = 0
			const val DATA_INDEX = 1
		}

	}
}