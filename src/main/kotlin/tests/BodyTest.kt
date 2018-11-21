package tests

import ecs.components.AccelerationComponent
import ecs.components.EnergyComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Core
import engine.entity.Entity
import engine.entity.EntityManager
import engine.events.UpdateManager
import engine.input.Input
import engine.physics.Circle
import engine.physics.Physics
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.engines.MockPhysicsEngine
import game.levels.EntityCreator
import general.Double2
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.KeyboardEventInit
import tests.physics.MockBody
import kotlin.browser.document

class BodyTest : ITest {
	private var testEntity: Entity = Entity(-1)

	private val key = 'd'

	override fun run() {
		initialize()
		test()
		finalize()
	}

	private fun initialize() {
		Core
		dispatchEvent(EventType.KeyDown, key)
		Input.update(1.0 / 60.0)
		Assert.isTrue(Input.right.isDown())

		val engine = MockPhysicsEngine()
		Physics.engine = engine

		testEntity = EntityCreator.createWithBody {
			bodyBuilder = MutableBodyBuilder(Circle(10.0), BodyMotionType.Dynamic)
			isPlayer = true
			addComponent { EnergyComponent(100.0, 50.0, 10.0) }
		}
	}

	private fun test() {
		val body = testEntity.getComponent<PhysicsEntityComponent>().body.unsafeCast<MockBody>()

		val acceleration = testEntity.getComponent<AccelerationComponent>().value

		Assert.equals(Circle(10.0), body.shape)
		Assert.equals(BodyMotionType.Dynamic, body.motionType)

		for (i in 1..10)
			UpdateManager.update(0.1)

		var sum = 0.0
		var velocity = 0.0
		for (i in 1..10) {
			velocity += acceleration.x * 0.1
			sum += velocity * 0.1
		}

		Assert.equals(Double2(sum, 0), body.position)
	}

	private fun finalize() {
		dispatchEvent(EventType.KeyUp, key)
		Input.update(1.0 / 60.0)

		EntityManager.removeEntity(testEntity)
	}

	private fun dispatchEvent(type: EventType, key: Char) {
		val event =
			KeyboardEvent(type.toString(), KeyboardEventInit(key = key.toString(), code = "Key${key.toUpperCase()}"))
		document.dispatchEvent(event)
	}

	private enum class EventType {
		KeyDown {
			override fun toString() = "keydown"
		},
		KeyUp {
			override fun toString() = "keyup"
		}
	}

}