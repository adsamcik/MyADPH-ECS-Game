package ecs.component

import engine.component.IComponent
import utility.Double2

data class UserControlledComponent(val acceleration: Double2) : IComponent {
	constructor(velocityX: Double, velocityY: Double) : this(Double2(velocityX, velocityY))
}