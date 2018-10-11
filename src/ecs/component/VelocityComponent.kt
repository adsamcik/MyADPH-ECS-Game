package ecs.component

import engine.component.IComponent
import utility.Double2

data class VelocityComponent(val vector: Double2) : IComponent {
	var x
		get() = vector.x
		set(value) {
			vector.x = value
		}

	var y
		get() = vector.y
		set(value) {
			vector.y = value
		}

	constructor(x: Double, y: Double) : this(Double2(x, y))
}