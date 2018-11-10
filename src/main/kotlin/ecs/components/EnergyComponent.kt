package ecs.components

import engine.component.IComponent

data class EnergyComponent(
	var energy: Double,
	var rechargeSpeed: Double,
	var maxEnergyUsage: Double,
	val maxEnergy: Double,
	var currentDraw: Double = 0.0,
	var lastUseTime: Double = Double.MIN_VALUE
) : IComponent