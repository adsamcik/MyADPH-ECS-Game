package engine.system.requirements

import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import kotlin.reflect.KClass

data class ECInclusionNode(override val value: KClass<out IComponent>) :
	IValueNode<Entity, KClass<out IComponent>> {
	override fun evaluate(value: Entity) = EntityManager.hasComponent(value, this.value)
}

data class ECExclusionNode(override val value: KClass<out IComponent>) :
	IValueNode<Entity, KClass<out IComponent>> {
	override fun evaluate(value: Entity) = !EntityManager.hasComponent(value, this.value)
}

fun INode<Entity>.andInclude(componentType: KClass<out IComponent>) = and(
	ECInclusionNode(
		componentType
	)
)
fun INode<Entity>.andExclude(componentType: KClass<out IComponent>) = and(
	ECExclusionNode(
		componentType
	)
)

fun INode<Entity>.orInclude(componentType: KClass<out IComponent>) = or(
	ECInclusionNode(
		componentType
	)
)
fun INode<Entity>.orExclude(componentType: KClass<out IComponent>) = or(
	ECExclusionNode(
		componentType
	)
)

