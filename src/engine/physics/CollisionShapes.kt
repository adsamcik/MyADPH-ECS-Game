package engine.physics

import utility.Double2

interface ColliderShape {

	fun collidesWith(shape: ColliderShape): Array<CollisionData> {
		return if (bounds.collidesWith(position, shape.position, shape.bounds))
			collidesWithNoBoundsCheck(shape)
		else
			emptyArray()
	}

	fun collidesWithNoBoundsCheck(shape: ColliderShape): Array<CollisionData>
	fun collidesWithNoBoundsCheck(rect: RectangleCollider): Array<CollisionData>
	fun collidesWithNoBoundsCheck(circle: CircleCollider): Array<CollisionData>
	fun collidesWithNoBoundsCheck(line: LineCollider): Array<CollisionData>

	val position: Double2
	val bounds: Bounds
}

data class CircleCollider(val center: Double2, val circle: Circle) : ColliderShape {
	override val bounds: Bounds = Bounds(-radius, -radius, radius, radius)

	override val position: Double2
		get() = center

	val radius: Double
		get() = circle.radius

	override fun collidesWithNoBoundsCheck(line: LineCollider): Array<CollisionData> = Intersections.circleLine(this, line)
	override fun collidesWithNoBoundsCheck(shape: ColliderShape) = shape.collidesWithNoBoundsCheck(this)

	override fun collidesWithNoBoundsCheck(rect: RectangleCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun collidesWithNoBoundsCheck(circle: CircleCollider): Array<CollisionData> = Intersections.circleCircle(this, circle)
}

data class RectangleCollider(override val position: Double2, val rectangle: Rectangle) : ColliderShape {
	override val bounds: Bounds = Bounds(0.0, 0.0, rectangle.width, rectangle.height)

	override fun collidesWithNoBoundsCheck(line: LineCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun collidesWithNoBoundsCheck(shape: ColliderShape) = shape.collidesWithNoBoundsCheck(this)

	override fun collidesWithNoBoundsCheck(rect: RectangleCollider): Array<CollisionData> {
		TODO("not implemented")
	}

	override fun collidesWithNoBoundsCheck(circle: CircleCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}

data class LineCollider(val start: Double2, val end: Double2) : ColliderShape {
	override val bounds: Bounds = Bounds(kotlin.math.min(start.x, end.x),
			kotlin.math.min(start.y, end.y),
			kotlin.math.max(start.x, end.x),
			kotlin.math.max(start.y, end.y))

	override val position: Double2
		get() = start

	override fun collidesWithNoBoundsCheck(line: LineCollider): Array<CollisionData> = Intersections.lineLine(this, line)

	override fun collidesWithNoBoundsCheck(shape: ColliderShape): Array<CollisionData> = shape.collidesWithNoBoundsCheck(this)

	override fun collidesWithNoBoundsCheck(rect: RectangleCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun collidesWithNoBoundsCheck(circle: CircleCollider): Array<CollisionData> = Intersections.circleLine(circle, this)
}

data class PolygonCollider(val points: List<Double2>) : ColliderShape {
	override fun collidesWithNoBoundsCheck(shape: ColliderShape): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun collidesWithNoBoundsCheck(rect: RectangleCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun collidesWithNoBoundsCheck(circle: CircleCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun collidesWithNoBoundsCheck(line: LineCollider): Array<CollisionData> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override val position: Double2
		get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

	override val bounds: Bounds

	init {
		if (points.size < 3)
			throw RuntimeException("Polygon cannot be made of less than three points")

		val it = points.iterator()

		var item = it.next()

		var left = item.x
		var right = item.x
		var top = item.y
		var bottom = item.y

		while (it.hasNext()) {
			item = it.next()
			if (item.x < left)
				left = item.x
			else if (item.x > right)
				right = item.x

			if (item.y < top)
				top = item.y
			else if (item.y > bottom)
				bottom = item.y
		}

		bounds = Bounds(left, top, right, bottom)
	}
}

object Intersections {
	//todo fix normals
	fun lineLine(line1: LineCollider, line2: LineCollider): Array<CollisionData> {

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
			arrayOf(CollisionData(line1, line2, result, (line2.end - line2.start).normalVector))
		else
			emptyArray()
	}

	fun circleCircle(c1: CircleCollider, c2: CircleCollider): Array<CollisionData> {
		val cd = c2.center - c1.center
		val cdNormalized = cd.normalized
		val cdMagnitude = cd.magnitude

		if (cdMagnitude > c2.radius + c1.radius)
			return emptyArray()

		return if (c1.radius > c2.radius)
			arrayOf(CollisionData(c1, c2, cdNormalized * c1.radius + c1.center, cdNormalized))
		else
			arrayOf(CollisionData(c1, c2, -cdNormalized * c2.radius + c2.center, cdNormalized))
	}


	//todo fix normals
	fun circleLine(circle: CircleCollider, line: LineCollider): Array<CollisionData> {
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
		val onLine1 = isOnLine(line.start, line.end, p1)
		if (disc == 0.0) { // abScalingFactor1 == abScalingFactor2
			return if (onLine1)
				arrayOf(CollisionData(circle, line, p1, (line.start - p1).normalVector))
			else
				emptyArray()
		}
		val p2 = Double2(line.start.x - baX * abScalingFactor2, line.start.y - baY * abScalingFactor2)
		val onLine2 = isOnLine(line.start, line.end, p2)
		return if (onLine1) {
			if (onLine2)
				arrayOf(CollisionData(circle, line, p1, (line.start - p1).normalVector), CollisionData(circle, line, p2, (line.start - p2).normalVector))
			else
				arrayOf(CollisionData(circle, line, p1, (line.start - p1).normalVector))
		} else if (onLine2)
			arrayOf(CollisionData(circle, line, p2, (line.start - p2).normalVector))
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