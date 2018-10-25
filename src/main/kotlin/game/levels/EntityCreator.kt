package game.levels

import ecs.components.*
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import engine.Graphics
import engine.PhysicsEngine
import engine.entity.Entity
import engine.entity.EntityComponentsBuilder
import engine.entity.EntityManager
import engine.physics.BodyBuilder
import game.modifiers.IModifierFactory
import game.modifiers.ModifierCommandFactory
import jslib.Matter
import jslib.pixi.Container
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class EntityCreator {
	private var _bodyBuilder: BodyBuilder? = null

	@Transient
	private val bodyBuilder: BodyBuilder
		get() = _bodyBuilder ?: throw IllegalStateException("Body builder must be set before building")

	private var isPlayer = false

	private var canReceiveModifiers = false

	private var modifierFactory = ModifierCommandFactory()

	private var follow = false

	fun setBodyBuilder(bodyBuilder: BodyBuilder) {
		this._bodyBuilder = bodyBuilder
	}

	fun setPlayer(isPlayer: Boolean) {
		this.isPlayer = isPlayer
	}

	fun setFollow(follow: Boolean) {
		this.follow = follow
	}

	fun setReceiveModifiers(canReceiveModifiers: Boolean) {
		this.canReceiveModifiers = canReceiveModifiers
	}

	fun addModifier(factory: IModifierFactory) {
		this.modifierFactory.addModifier(factory)
	}

	fun create(): Entity {
		val (body, graphics) = bodyBuilder.build()
		val container = if (body.isStatic) Graphics.staticBackgroundContainer else Graphics.dynamicContainer

		return create(container, PhysicsEngine.world, body, graphics)
	}

	fun create(container: Container, world: Matter.World = PhysicsEngine.world): Entity {
		val (body, graphics) = bodyBuilder.build()
		return create(container, world, body, graphics)
	}

	fun create(container: Container, world: Matter.World, body: Matter.Body, graphics: jslib.pixi.Graphics): Entity {
		return EntityManager.createEntity {
			addGraphics(this, container, graphics)
			addPhysics(it, this, world, body)

			if (modifierFactory.isNotEmpty) {
				modifierFactory.setEntity(it)
				addComponent(ModifierSpreaderComponent(modifierFactory))
			}

			if (isPlayer)
				addComponent(PlayerComponent())

			if (canReceiveModifiers)
				addComponent(ModifierReceiverComponent(it, bodyBuilder))

			if (follow)
				addComponent(DisplayFollowComponent())
		}
	}

	private fun addGraphics(
		entityBuilder: EntityComponentsBuilder,
		container: Container,
		graphics: jslib.pixi.Graphics
	) {
		container.addChild(graphics)
		entityBuilder.addComponent(GraphicsComponent(graphics))
	}

	private fun addPhysics(
		entity: Entity,
		entityBuilder: EntityComponentsBuilder,
		world: Matter.World,
		body: Matter.Body
	) {
		body.entity = entity

		entityBuilder.addComponent(PhysicsEntityComponent(body))
		Matter.World.add(world, body)

		if (!body.isStatic)
			entityBuilder.addComponent(PhysicsDynamicEntityComponent())
	}


	companion object {
		fun create(
			container: Container,
			world: Matter.World = PhysicsEngine.world,
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).create(container, world)
		}
	}
}