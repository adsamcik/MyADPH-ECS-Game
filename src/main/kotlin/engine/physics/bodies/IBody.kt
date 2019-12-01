package engine.physics.bodies

import engine.entity.Entity
import engine.interfaces.IMemento
import engine.interfaces.IMementoClass
import general.Double2

//PATTERN Proxy
interface IBody {
	var position: Double2
	var velocity: Double2

	var angle: Double
	var angleRadians: Double

	var motionType: BodyMotionType
	var friction: Double
	var restitution: Double
	var isSensor: Boolean
	var entity: Entity
	var density: Double
	var mass: Double
	var isEnabled: Boolean
	var isAwake: Boolean
	val filter: IFilter

	fun applyForce(position: Double2, force: Double2)
	fun applyForce(force: Double2)

	fun rotate(degrees: Double)

	fun destroy()

	//exposes only memento interface because it should be black box
	fun save(): IMemento = Memento(position, velocity,
		angleRadians, motionType, friction, restitution, entity)

	fun saveWithoutVelocity(): IMemento = Memento(position, Double2(), angleRadians, motionType, friction, restitution, entity)

	fun restore(memento: IMemento) {
		if (memento is IBody.Memento)
			restore(memento)
		else
			throw IllegalArgumentException("Expected memento of type ${IBody.Memento::class} but got ${memento::class}")

		isAwake = true
	}

	private fun restore(memento: Memento) {
		position = memento.position
		velocity = memento.velocity
		angleRadians = memento.angleRadians
		motionType = memento.bodyMotionType
		friction = memento.friction
		restitution = memento.restitution
		entity = memento.entity
	}

	interface IFilter : IMementoClass {
		var group: Int
		var category: Int
		var mask: Int

		fun set(group: Int, category: Int, mask: Int)

		override fun restore(memento: IMemento) {
			memento as Memento
			group = memento.group
			category = memento.category
			mask = memento.mask
		}

		override fun save() = Memento(group, category, mask)

		data class Memento(val group: Int, val category: Int, val mask: Int) : IMemento
	}

	data class Memento(
		val position: Double2,
		val velocity: Double2,
		val angleRadians: Double,
		val bodyMotionType: BodyMotionType,
		val friction: Double,
		val restitution: Double,
		val entity: Entity
	) : IMemento
}

enum class BodyMotionType {
	Static,
	Kinematic,
	Dynamic
}