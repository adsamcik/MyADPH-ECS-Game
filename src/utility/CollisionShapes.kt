package utility

interface IColliderShape {
    fun checkCollision(shape: IColliderShape): Boolean
    fun checkCollision(rect: RectangleCollider): Boolean
    fun checkCollision(circle: CircleCollider): Boolean

    val double2: Double2
}

data class CircleCollider(override val double2: Double2, val radius: Double) : IColliderShape {
    override fun checkCollision(shape: IColliderShape): Boolean = shape.checkCollision(this)

    override fun checkCollision(rect: RectangleCollider): Boolean {
        return false
    }

    override fun checkCollision(circle: CircleCollider): Boolean {
        val collisionDistance = this.radius + circle.radius
        return this.double2.distance(circle.double2) <= collisionDistance
    }
}

data class RectangleCollider(override val double2: Double2, val width: Double, val height: Double) : IColliderShape {
    override fun checkCollision(shape: IColliderShape): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCollision(rect: RectangleCollider): Boolean {
        return this.double2.x < rect.double2.x + rect.width &&
                this.double2.x + this.width > rect.double2.x &&
                this.double2.y > rect.double2.y - rect.height &&
                this.double2.y - this.height < rect.double2.y
    }

    override fun checkCollision(circle: CircleCollider): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}