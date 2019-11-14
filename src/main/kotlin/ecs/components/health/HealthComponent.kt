package ecs.components.health

import engine.component.IComponent
import kotlinx.serialization.Serializable

@Serializable
data class HealthComponent(
	var health: Double,
	val maxHealth: Double,
	val damagers: MutableSet<DamageComponent> = mutableSetOf()
) : IComponent {
	constructor(maxHealth: Double) : this(maxHealth, maxHealth)
}