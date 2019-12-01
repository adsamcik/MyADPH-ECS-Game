package ecs.components.health

import engine.component.IComponent
import kotlinx.serialization.Serializable

@Serializable
data class HealthComponent(
	var health: Double,
	var maxHealth: Double,
	val damageList: MutableSet<DamageComponent> = mutableSetOf()
) : IComponent {
	constructor(maxHealth: Double) : this(maxHealth, maxHealth)
}