package game.modifiers

import ecs.components.BodyBuilderComponent
import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.interfaces.IMemento

interface IModifierLogic {
	val modifiers: Collection<IModifier>
	val hasNoModifiers: Boolean

	fun update(deltaTime: Double)

	fun setModifier(modifier: IModifier)
	fun removeModifier(modifier: IModifier)
}

abstract class ModifierLogic<T : IModifier>(protected val entity: Entity) : IModifierLogic {
	private var currentModifier: T? = null

	private val _modifiers = mutableListOf<T>()

	override val modifiers: Collection<IModifier>
		get() = _modifiers

	override val hasNoModifiers: Boolean
		get() = _modifiers.isEmpty()

	override fun setModifier(modifier: IModifier) {
		//checking if it's safe is actually pretty difficult with generics
		setModifier(modifier.unsafeCast<T>())
	}

	override fun removeModifier(modifier: IModifier) {
		removeModifier(modifier.unsafeCast<T>())
	}

	fun setModifier(modifier: T) {
		if (_modifiers.isEmpty())
			save()


		val currentModifier = currentModifier
		if (currentModifier?.entity == modifier.entity) {
			currentModifier.timeLeft = modifier.timeLeft
			currentModifier.state = modifier.state
			return
		}

		val found = _modifiers.find { it.entity == modifier.entity }

		if (found != null) {
			found.timeLeft = modifier.timeLeft
			found.state = modifier.state
		} else
			_modifiers.add(modifier)

		applyModifierInternal(modifier)
	}

	private fun applyModifierInternal(modifier: T) {
		currentModifier = modifier
		applyModifier(modifier)
	}

	protected abstract fun applyModifier(modifier: T)
	protected abstract fun restoreDefault()
	protected abstract fun save()

	fun removeModifier(modifier: T) {
		_modifiers.remove(modifier)
		if (modifier == currentModifier)
			onCurrentRemoved()
	}

	private fun onCurrentRemoved() {
		if (_modifiers.isNotEmpty()) {
			val last = _modifiers.last()
			applyModifier(last)
		} else {
			currentModifier = null
			restoreDefault()
		}
	}

	fun removeModifiers(predicate: (T) -> Boolean) {
		if (_modifiers.removeAll(predicate) && !_modifiers.contains(currentModifier)) {
			onCurrentRemoved()
		}
	}

	override fun update(deltaTime: Double) {
		_modifiers.forEach { it.timeLeft -= deltaTime }

		removeModifiers { it.timeLeft <= 0 }

		internalUpdate(deltaTime)
	}

	protected open fun internalUpdate(deltaTime: Double) {}
}

class ShapeModifierLogic(entity: Entity) : ModifierLogic<ShapeModifier>(entity) {
	private lateinit var physicsMemento: IMemento

	override fun save() {
		physicsMemento = entity.getComponent(PhysicsEntityComponent::class).save()
	}

	override fun applyModifier(modifier: ShapeModifier) {
		val body = modifier.bodyBuilder.buildBody(entity)
		val graphics = modifier.bodyBuilder.buildGraphics()

		val physicsComponent = entity.getComponent(PhysicsEntityComponent::class)

		val state = physicsComponent.saveProperties()

		body.restore(state)

		Graphics.dynamicContainer.addChild(graphics)
		EntityManager.setComponents(
			entity,
			GraphicsComponent(graphics),
			PhysicsEntityComponent(body, modifier.bodyBuilder.shape!!)
		)
	}

	override fun restoreDefault() {
		entity.getComponent(PhysicsEntityComponent::class).restoreShape(physicsMemento)

		val bodyBuilder = entity.getComponent(BodyBuilderComponent::class).bodyBuilder

		val graphicsComponent = GraphicsComponent(bodyBuilder.buildGraphics())

		val container = Graphics.getContainer(bodyBuilder.motionType)
		container.addChild(graphicsComponent.value)
		EntityManager.setComponent(entity, graphicsComponent)
	}
}

class RestitutionModifierLogic(entity: Entity) : ModifierLogic<RestitutionModifier>(entity) {
	override fun applyModifier(modifier: RestitutionModifier) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun restoreDefault() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun save() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}