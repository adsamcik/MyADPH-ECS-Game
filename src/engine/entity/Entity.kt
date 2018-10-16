package engine.entity

import engine.component.IComponent
import kotlin.reflect.KClass

data class Entity(val id: Int) {
	fun <T> getComponent(type: KClass<out T>): T where T : IComponent = EntityManager.getComponent(this, type)
}