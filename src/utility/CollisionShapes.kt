package utility

interface IColliderShape {

    /**
     * Checks for collisions and returns collision points
     */
    fun checkCollision(shape: IColliderShape): Array<Double2>

    fun checkCollision(rect: RectangleCollider): Array<Double2>
    fun checkCollision(circle: CircleCollider): Array<Double2>
    fun checkCollision(line: LineCollider): Array<Double2>

    val position: Double2
}

data class CircleCollider(val center: Double2, val radius: Double) : IColliderShape {
    override val position: Double2
        get() = center

    override fun checkCollision(line: LineCollider): Array<Double2> = Intersections.circleLine(this, line)
    override fun checkCollision(shape: IColliderShape) = shape.checkCollision(this)

    override fun checkCollision(rect: RectangleCollider): Array<Double2> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCollision(circle: CircleCollider): Array<Double2> = Intersections.circleCircle(this, circle)
}

data class RectangleCollider(override val position: Double2, val width: Double, val height: Double) : IColliderShape {
    override fun checkCollision(line: LineCollider): Array<Double2> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCollision(shape: IColliderShape) = shape.checkCollision(this)

    override fun checkCollision(rect: RectangleCollider): Array<Double2> {
        TODO("not implemented")
    }

    override fun checkCollision(circle: CircleCollider): Array<Double2> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

data class LineCollider(val start: Double2, val end: Double2) : IColliderShape {

    override val position: Double2
        get() = start

    override fun checkCollision(line: LineCollider): Array<Double2> = Intersections.lineLine(this, line)

    override fun checkCollision(shape: IColliderShape): Array<Double2> = shape.checkCollision(this)

    override fun checkCollision(rect: RectangleCollider): Array<Double2> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkCollision(circle: CircleCollider): Array<Double2> = Intersections.circleLine(circle, this)
}

object Intersections {
    fun lineLine(line1: LineCollider, line2: LineCollider): Array<Double2> {

        // if the lines intersect, the result contains the x and y of the intersection (treating the lines as infinite) and booleans for whether line segment 1 or line segment 2 contain the point
        val denominator = ((line2.end.y - line2.start.y) * (line1.end.x - line1.start.x)) - ((line2.end.x - line2.start.x) * (line1.end.y - line1.start.y))
        if (denominator == 0.0)
            return emptyArray()

        var a = line1.start.y - line2.start.y
        var b = line1.start.x - line2.start.x
        val numerator1 = ((line2.end.x - line2.start.x) * a) - ((line2.end.y - line2.start.y) * b)
        val numerator2 = ((line1.end.x - line1.start.x) * a) - ((line1.end.y - line1.start.y) * b)
        a = numerator1 / denominator
        b = numerator2 / denominator

        // if we cast these lines infinitely in both directions, they intersect here:
        val result = Double2(line1.start.x + (a * (line1.end.x - line1.start.x)), line1.start.y + (a * (line1.end.y - line1.start.y)))
        /*
                // it is worth noting that this should be the same as:
                x = start2.x + (b * (end2.x - start2.x));
                y = start2.x + (b * (end2.y - start2.y));
                */
        return if (a > 0 && a < 1 && b > 0 && b < 1)
            arrayOf(result)
        else
            emptyArray()
    }

    fun circleCircle(c1: CircleCollider, c2: CircleCollider): Array<Double2> {
        val cd = c2.center - c1.center
        val cdm = cd.magnitude

        if (cdm > c2.radius + c1.radius)
            return emptyArray()

        val k = (cdm * cdm + c1.radius * c1.radius - c2.radius * c2.radius) / (2 * cdm)

        return arrayOf(
                Double2(c1.center.x + cd.x * k / cdm + (cd.y / cdm) * kotlin.math.sqrt(c1.radius * c1.radius - k * k), c1.center.y + cd.y * k / cdm - (cd.x / cdm) * kotlin.math.sqrt(c1.radius * c1.radius - k * k)),
                Double2(c1.center.x + cd.x * k / cdm - (cd.y / cdm) * kotlin.math.sqrt(c1.radius * c1.radius - k * k), c1.center.y + cd.y * k / cdm + (cd.x / cdm) * kotlin.math.sqrt(c1.radius * c1.radius - k * k))
        )
    }




    fun circleLine(circle: CircleCollider, line: LineCollider): Array<Double2> {
        val baX = line.end.x - line.start.x
        val baY = line.end.y - line.start.y
        val caX = circle.center.x - line.start.x
        val caY = circle.center.y - line.start.y

        val a = baX * baX + baY * baY
        val bBy2 = baX * caX + baY * caY
        val c = caX * caX + caY * caY - circle.radius * circle.radius

        val pBy2 = bBy2 / a
        val q = c / a

        val disc = pBy2 * pBy2 - q
        if (disc < 0)
            return emptyArray()
        // if disc == 0 ... dealt with later
        val tmpSqrt = kotlin.math.sqrt(disc)
        val abScalingFactor1 = -pBy2 + tmpSqrt
        val abScalingFactor2 = -pBy2 - tmpSqrt

        val p1 = Double2(line.start.x - baX * abScalingFactor1, line.start.y - baY * abScalingFactor1)
        val onLine1 = this.isOnLine(line.start, line.end, p1)
        if (disc == 0.0) { // abScalingFactor1 == abScalingFactor2
            return if (onLine1)
                arrayOf(p1)
            else
                emptyArray()
        }
        val p2 = Double2(line.start.x - baX * abScalingFactor2, line.start.y - baY * abScalingFactor2)
        val onLine2 = this.isOnLine(line.start, line.end, p2)
        return if (onLine1) {
            if (onLine2)
                arrayOf(p1, p2)
            else
                arrayOf(p1)
        } else if (onLine2)
            arrayOf(p2)
        else
            emptyArray()
    }

    private fun isOnLine(a: Double2, b: Double2, c: Double2): Boolean {
        // Compute the dot product of vectors
        val ab = a - b
        val ac = a - c
        val kac = ab.dot(ac)
        if (kac <= 0.0) return false

        // Compute the square of the segment length
        val kab = ab.dot(ab)
        if (kac >= kab) return false
        return true
    }
}