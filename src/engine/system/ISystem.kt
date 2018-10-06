package engine.system

import engine.entity.Entity

interface ISystem {
    fun update(deltaTime: Double, entities: Collection<Entity>)

    fun componentSpecification(): Collection<ComponentRequirement>
}