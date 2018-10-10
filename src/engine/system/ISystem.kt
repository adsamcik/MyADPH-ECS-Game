package engine.system

import engine.entity.Entity
import utility.INode

interface ISystem {
	fun update(deltaTime: Double, entities: Collection<Entity>)

	val requirements: INode<Entity>
}