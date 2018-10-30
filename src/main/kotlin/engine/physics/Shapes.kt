package engine.physics

import engine.serialization.GenericSerializer
import kotlinx.serialization.*
import utility.Double2

@Serializable(with = ShapeSerializer::class)
interface IShape

@Serializable
data class Circle(val radius: Double) : IShape

@Serializable
data class Rectangle(val width: Double, val height: Double) : IShape

@Serializable
data class Polygon(val points: Collection<Double2>) : IShape

@Serializer(forClass = IShape::class)
object ShapeSerializer : GenericSerializer<IShape>("shape") {
	override val descriptor: SerialDescriptor
		get() = super.descriptor

	override fun deserialize(input: Decoder): IShape {
		return super.deserialize(input)
	}

	override fun deserialize(type: String, structure: CompositeDecoder) = when (type) {
		Circle::class.simpleName -> structure.decodeSerializableElement(
			descriptor,
			StructureDescriptor.DATA_INDEX,
			Circle.serializer()
		)
		Rectangle::class.simpleName -> structure.decodeSerializableElement(
			descriptor,
			StructureDescriptor.DATA_INDEX,
			Rectangle.serializer()
		)
		Polygon::class.simpleName -> structure.decodeSerializableElement(
			descriptor,
			StructureDescriptor.DATA_INDEX,
			Polygon.serializer()
		)
		else -> throw Error("Deserializer for $type not implemented")
	}

	override fun serialize(output: Encoder, obj: IShape) {
		when (obj) {
			is Circle -> serialize(output, obj, Circle.serializer())
			is Rectangle -> serialize(output, obj, Rectangle.serializer())
			is Polygon -> serialize(output, obj, Polygon.serializer())
			else -> throw Error("Serializer for ${obj::class.simpleName} not implemented")
		}
	}
}