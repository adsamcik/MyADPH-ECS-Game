package utility

//pattern double dispatch
interface IShape {
	val bounds: Bounds
}

data class Circle(val radius: Double) : IShape {
	override val bounds: Bounds = Bounds(-radius, -radius, radius, radius)
}

data class Rectangle(val width: Double, val height: Double) : IShape {
	override val bounds: Bounds = Bounds(0.0, 0.0, width, height)
}

data class Polygon(val points: Collection<Double2>) : IShape {
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