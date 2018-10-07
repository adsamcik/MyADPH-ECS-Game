package ecs.component

import engine.component.IComponent
import utility.Double2

data class PositionComponent(val double2: Double2) : IComponent {
    var x
        get() = double2.x
        set(value) {
            double2.x = value
        }

    var y
        get() = double2.y
        set(value) {
            double2.y = value
        }


    val xInt
        get() = kotlin.math.round(double2.x)

    val yInt
        get() = kotlin.math.round(double2.y)

    constructor(x: Double, y: Double) : this(Double2(x, y))
}