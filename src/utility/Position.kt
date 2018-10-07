package utility

data class Position(var x: Double, var y: Double) {
    val sqrMagnitude
        get() = x * x + y * y

    val magnitude
        get() = kotlin.math.sqrt(sqrMagnitude)

    fun difference(position: Position): Position = Position(position.x - this.x, position.y - this.y)

    fun distance(position: Position): Double {
        return difference(position).magnitude
    }
}