package ecs.components

import engine.component.IComponent
import kotlinx.serialization.Serializable

@Serializable
data class EnergyComponent(
	var energy: Double,
	var rechargeSpeed: Double,
	var maxEnergyUsage: Double,
	var maxEnergy: Double,
	var currentDraw: Double = 0.0,
	var lastUseTime: Double = Double.MIN_VALUE
) : IComponent {
	constructor(maxEnergy: Double, rechargeSpeed: Double, maxEnergyUsage: Double) : this(
		maxEnergy,
		rechargeSpeed,
		maxEnergyUsage,
		maxEnergy
	)
}