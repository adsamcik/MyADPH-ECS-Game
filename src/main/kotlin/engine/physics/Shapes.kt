package engine.physics

import jslib.Matter
import kotlinx.serialization.*
import utility.Double2

@Serializable(with = ShapeSerializer::class)
interface IShape {
	fun buildBody(position: Double2): Matter.Body

}

@Serializable
data class Circle(val radius: Double) : IShape {
	override fun buildBody(position: Double2): Matter.Body = Matter.Bodies.circle(position.x, position.y, radius)
}

@Serializable
data class Rectangle(val width: Double, val height: Double) : IShape {
	override fun buildBody(position: Double2): Matter.Body =
		Matter.Bodies.rectangle(position.x, position.y, width, height)
}

@Serializable
data class Polygon(val points: Collection<Double2>) : IShape {
	override fun buildBody(position: Double2): Matter.Body =
		Matter.Bodies.fromVertices(position.x, position.y, points.toTypedArray())
}

@Serializer(IShape::class)
class ShapeSerializer {
	override fun deserialize(input: Decoder): IShape {
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