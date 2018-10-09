package ecs.component

import engine.component.IComponent
import utility.Double2

data class PositionComponent(val position: Double2) : IComponent {
    var x
        get() = position.x
        set(value) {
            position.x = value
        }

    var y
        get() = position.y
        set(value) {
            position.y = value
        }


    val xInt
        get() = kotlin.math.round(position.x)

    val yInt
        get() = kotlin.math.round(position.y)

    constructor(x: Double, y: Double) : this(Double2(x, y))
}

data class OriginalPositionComponent(var position: Double2) : IComponent