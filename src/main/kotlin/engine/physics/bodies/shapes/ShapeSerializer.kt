package engine.physics.bodies.shapes

import engine.serialization.GenericSerializer
import kotlinx.serialization.*

@Serializer(forClass = IShape::class)
object ShapeSerializer : GenericSerializer<IShape>("shape") {
	override val descriptor: SerialDescriptor
		get() = super.descriptor

	override fun deserialize(decoder: Decoder): IShape {
		return super.deserialize(decoder)
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

	override fun serialize(encoder: Encoder, obj: IShape) {
		when (obj) {
			is Circle -> serialize(encoder, obj, Circle.serializer())
			is Rectangle -> serialize(encoder, obj, Rectangle.serializer())
			is Polygon -> serialize(encoder, obj, Polygon.serializer())
			else -> throw Error("Serializer for ${obj::class.simpleName} not implemented")
		}
	}
}