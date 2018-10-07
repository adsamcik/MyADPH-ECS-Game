package utility

//pattern double dispatch
interface IShape {
    fun checkCollision(shape: IShape): Boolean
    fun checkCollision(rect: Rectangle): Boolean
    fun checkCollision(circle: Circle): Boolean

    val double2: Double2
}

data class Circle(override val double2: Double2, val radius: Double) : IShape {
    override fun checkCollision(shape: IShape): Boolean = shape.checkCollision(this)

    override fun checkCollision(rect: Rectangle): Boolean {
        return false
    }

    override fun checkCollision(circle: Circle): Boolean {
        val collisionDistance = this.radius + circle.radius
        return this.double2.distance(circle.double2) <= collisionDistance
    }

}

data class Rectangle(override val double2: Double2, val width: Double, val height: Double) : IShape {
    override fun checkCollision(shape: IShape): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCollision(rect: Rectangle): Boolean {
        return this.double2.x < rect.double2.x + rect.width &&
                this.double2.x + this.width > rect.double2.x &&
                this.double2.y > rect.double2.y - rect.height &&
                this.double2.y - this.height < rect.double2.y
    }

    override fun checkCollision(circle: Circle): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}