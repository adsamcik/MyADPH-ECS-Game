package ecs.components

import engine.component.IComponent
import utility.Double2

data class PositionComponent(val value: Double2) : IComponent {
	var x
		get() = value.x
		set(value) {
			this.value.x = value
		}

	var y
		get() = value.y
		set(value) {
			this.value.y = value
		}


	val xInt
		get() = kotlin.math.round(value.x)

	val yInt
		get() = kotlin.math.round(value.y)

	constructor(x: Double, y: Double) : this(Double2(x, y))
}

data class OriginalPositionComponent(var position: Double2) : IComponent