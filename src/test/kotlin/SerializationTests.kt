import engine.physics.IShape
import engine.physics.Rectangle
import kotlin.test.*

class SimpleTest {

	@Test fun testShapeSerializer() {
		val rectangle = Rectangle(10.0, 20.0)
		val rSrcSerialized = JSON.stringify(rectangle)

		val rTestSerialized = JSON.stringify(rectangle as IShape)
		assertEquals(rSrcSerialized, rTestSerialized, "Serialization failed")
	}
}