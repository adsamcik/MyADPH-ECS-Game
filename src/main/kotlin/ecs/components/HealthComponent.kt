package ecs.components

import engine.component.IComponent

data class HealthComponent(
	var health: Double,
	val maxHealth: Double,
	val damagers: MutableSet<DamageComponent> = mutableSetOf()
) : IComponent