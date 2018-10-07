package utility

//pattern double dispatch
interface IShape {
    fun checkCollision(shape: IShape): Boolean
    fun checkCollision(rect: Rectangle): Boolean
    fun checkCollision(circle: Circle): Boolean

    val position: Position
}

data class Circle(override val position: Position, val radius: Double) : IShape {
    override fun checkCollision(shape: IShape): Boolean = shape.checkCollision(this)

    override fun checkCollision(rect: Rectangle): Boolean {
        return false
    }

    override fun checkCollision(circle: Circle): Boolean {
        val collisionDistance = this.radius + circle.radius
        return this.position.distance(circle.position) <= collisionDistance
    }

}

data class Rectangle(override val position: Position, val width: Double, val height: Double) : IShape {
    override fun checkCollision(shape: IShape): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCollision(rect: Rectangle): Boolean {
        return this.position.x < rect.position.x + rect.width &&
                this.position.x + this.width > rect.position.x &&
                this.position.y > rect.position.y - rect.height &&
                this.position.y - this.height < rect.position.y
    }

    override fun checkCollision(circle: Circle): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}