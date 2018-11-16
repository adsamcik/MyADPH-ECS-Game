package game.levels

import ecs.components.*
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
import engine.physics.bodies.builder.IBodyBuilder
import game.modifiers.IModifierFactory
import game.modifiers.ModifierCommandFactory
import jslib.pixi.Container
import kotlinx.serialization.Serializable

typealias ComponentFactory = () -> IComponent

@Serializable
class EntityCreator {

	private var bodyBuilder: IBodyBuilder? = null

	private var isPlayer = false

	private var canReceiveModifiers = false

	private var modifierFactory = ModifierCommandFactory()

	private var follow = false


	private val componentList = mutableListOf<ComponentFactory>()

	fun setBodyBuilder(bodyBuilder: IBodyBuilder) {
		this.bodyBuilder = bodyBuilder
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


	fun createWithBody() = createWithBody(Graphics.getContainer(bodyBuilder!!.motionType))

	fun createWithBody(container: Container): Entity {
		return EntityManager.createEntity {
			buildBody(this, container, bodyBuilder!!, it)
			buildComponents(this, it)
		}
	}

	fun createWithoutBody(): Entity {
		return EntityManager.createEntity {
			buildComponents(this, it)
		}
	}

	private fun buildComponents(creator: EntityComponentsBuilder, entity: Entity) {
		creator.apply {
			if (modifierFactory.isNotEmpty) {
				modifierFactory.setSourceEntity(entity)
				addComponent(ModifierSpreaderComponent(modifierFactory))
			}

			if (isPlayer)
				addComponent(PlayerComponent())

			if (canReceiveModifiers)
				addComponent(ModifierReceiverComponent(entity))

			if (follow)
				addComponent(DisplayFollowComponent())

			componentList.forEach { factory ->
				addComponent(factory.invoke())
			}
		}
	}

	private fun buildBody(
		entityBuilder: EntityComponentsBuilder,
		container: Container,
		bodyBuilder: IBodyBuilder,
		entity: Entity
	) {
		addGraphics(entityBuilder, container, bodyBuilder.buildGraphics())
		addPhysics(entityBuilder, bodyBuilder.buildBody(entity))

		entityBuilder.addComponent(DefaultBodyComponent(bodyBuilder))
		entityBuilder.addComponent(BodyComponent(bodyBuilder))
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
		fun createWithBody(
			container: Container,
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).createWithBody(container)
		}

		fun createWithBody(
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).createWithBody()
		}

		fun createWithoutBody(
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).createWithoutBody()
		}
	}
}