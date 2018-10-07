package ecs.component

import engine.component.IComponent
import utility.Position

data class PositionComponent(val position: Position) : IComponent {
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

    constructor(x: Double, y: Double) : this(Position(x, y))
}