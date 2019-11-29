package engine.serialization

import engine.entity.Entity

internal interface IEntitySerializationProvider {
	val serializationEntityData: List<EntitySerializer.EntityData>

	fun getEntitySerializationData(entity: Entity): EntitySerializer.EntityData
}