package engine.system

import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import kotlin.reflect.KClass

data class ComponentRequirement(val componentType: KClass<out IComponent>, val inclusion: ComponentInclusion) {

    fun isMet(entity: Entity): Boolean {
        val entityHasComponent = EntityManager.hasComponent(entity, componentType)
        return if (this.inclusion === ComponentInclusion.MustHave && entityHasComponent)
            true
        else this.inclusion === ComponentInclusion.MustNotHave && !entityHasComponent
    }
}

enum class ComponentInclusion {
    MustHave,
    MustNotHave
}
