package ecs.component

import engine.component.IComponent
import utility.Double2

data class VelocityComponent(var x: Double, var y: Double) : IComponent {
	constructor(double2: Double2) : this(double2.x, double2.y)
}