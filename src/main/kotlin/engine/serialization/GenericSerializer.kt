package engine.serialization

import engine.physics.Circle
import engine.physics.IShape
import engine.physics.Polygon
import engine.physics.Rectangle
import kotlinx.serialization.*
import kotlin.reflect.KClass


abstract class GenericSerializer<T> : KSerializer<T> {

	private val serializers = hashMapOf<String, KSerializer<T>>()


	override fun deserialize(input: Decoder): T {
		val structure = input.beginStructure(StorageDescriptor, Circle.serializer(), Rectangle.serializer(), Polygon.serializer())

		structure.decodeElementIndex(StorageDescriptor)
		val name = structure.decodeStringElement(StorageDescriptor, 0)
		structure.decodeElementIndex(StorageDescriptor)
		val shape = when (name) {
			Circle::class.simpleName -> structure.decodeSerializableElement(StorageDescriptor, 1, Circle.serializer())
			Rectangle::class.simpleName -> structure.decodeSerializableElement(
				StorageDescriptor,
				1,
				Rectangle.serializer()
			)
			Polygon::class.simpleName -> structure.decodeSerializableElement(StorageDescriptor, 1, Polygon.serializer())
			else -> throw Error("Deserializer for $name not implemented")
		}
		structure.endStructure(StorageDescriptor)
		return shape
	}

	override fun serialize(output: Encoder, obj: IShape) {
		when (obj) {
			is Circle -> serialize(output, obj, Circle.serializer())
			is Rectangle -> serialize(output, obj, Rectangle.serializer())
			is Polygon -> serialize(output, obj, Polygon.serializer())
			else -> throw Error("Serializer for ${obj::class.simpleName} not implemented")
		}
	}

	private inline fun <reified T : IShape> serialize(output: Encoder, obj: T, serializer: KSerializer<T>) {
		val structure = output.beginStructure(StorageDescriptor, serializer)
		structure.encodeStringElement(StorageDescriptor, 0, T::class.simpleName!!)
		structure.encodeSerializableElement(StorageDescriptor, 1, serializer, obj)
		structure.endStructure(StorageDescriptor)
	}

	object StorageDescriptor : SerialDescriptor {
		override val kind: SerialKind
			get() = UnionKind.OBJECT

		override val name: String
			get() = "shapeData"

		override fun getElementIndex(name: String): Int {
			return when (name) {
				SHAPE_CLASS -> 0
				SHAPE -> 1
				else -> throw Error("Unexpected name $name")
			}
		}

		override fun getElementName(index: Int): String {
			return when (index) {
				0 -> SHAPE_CLASS
				1 -> SHAPE
				else -> throw Error("Unexpected index $index")
			}
		}

		override fun isElementOptional(index: Int): Boolean {
			return false
		}

		private const val SHAPE_CLASS = "shapeClass"
		private const val SHAPE = "shape"

	}
}