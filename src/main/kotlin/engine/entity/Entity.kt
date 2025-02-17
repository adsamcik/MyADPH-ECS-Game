package engine.entity

import engine.component.IComponent
import kotlinx.serialization.Serializable

inline class Entity(val id: Int) {
	inline fun <reified T: IComponent> getComponent(): T = EntityManager.getComponent(this, T::class)
	override fun toString() = id.toString()
}