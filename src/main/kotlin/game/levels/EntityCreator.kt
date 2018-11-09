package game.levels

import ecs.components.DefaultBodyComponent
import ecs.components.DisplayFollowComponent
import ecs.components.GraphicsComponent
import ecs.components.PlayerComponent
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityComponentsBuilder
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import engine.physics.bodies.builder.MutableBodyBuilder
import game.modifiers.IModifierFactory
import game.modifiers.ModifierCommandFactory
import jslib.pixi.Container
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

typealias ComponentFactory = () -> IComponent

@Serializable
class EntityCreator {

	private var _bodyBuilder: MutableBodyBuilder? = null

	@Transient
	private val bodyBuilder: MutableBodyBuilder
		get() = _bodyBuilder ?: throw IllegalStateException("Body builder must be set before building")

	private var isPlayer = false

	private var canReceiveModifiers = false

	private var modifierFactory = ModifierCommandFactory()

	private var follow = false


	private val componentList = mutableListOf<ComponentFactory>()

	fun setBodyBuilder(bodyBuilder: MutableBodyBuilder) {
		if (isPlayer)
			console.log(bodyBuilder.motionType)
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

	fun addComponent(componentFactory: ComponentFactory) {
		this.componentList.add(componentFactory)
	}

	fun create() = create(Graphics.getContainer(bodyBuilder.motionType))

	private fun create(container: Container): Entity {
		return EntityManager.createEntity {
			addGraphics(this, container, bodyBuilder.buildGraphics())
			addPhysics(this, bodyBuilder.buildBody(it))

			addComponent(DefaultBodyComponent(bodyBuilder))

			if (modifierFactory.isNotEmpty) {
				modifierFactory.setSourceEntity(it)
				addComponent(ModifierSpreaderComponent(modifierFactory))
			}

			if (isPlayer)
				addComponent(PlayerComponent())

			if (canReceiveModifiers)
				addComponent(ModifierReceiverComponent(it))

			if (follow)
				addComponent(DisplayFollowComponent())

			componentList.forEach { factory ->
				addComponent(factory.invoke())
			}
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
		entityBuilder: EntityComponentsBuilder,
		body: IBody
	) {
		entityBuilder.addComponent(PhysicsEntityComponent(body))

		if (body.motionType == BodyMotionType.Dynamic) {
			entityBuilder.addComponent(PhysicsDynamicEntityComponent())
		}
	}


	companion object {
		fun create(
			container: Container,
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).create(container)
		}

		fun create(
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).create()
		}
	}
}