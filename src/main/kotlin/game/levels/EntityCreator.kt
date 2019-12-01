package game.levels

import ecs.components.*
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.physics.PhysicsKinematicEntityComponent
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityComponentsBuilder
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.IBody
import engine.physics.bodies.builder.IBodyBuilder
import game.modifiers.ModifierCommandFactory
import general.Double2
import definition.jslib.pixi.Container
import game.modifiers.factory.template.IModifierFactory
import kotlinx.serialization.Serializable

typealias ComponentFactory = () -> IComponent

@Serializable
class EntityCreator {
	var bodyBuilder: IBodyBuilder? = null
	var isPlayer = false
	var canReceiveModifiers = false
	var follow = false
	var usePhysics = true

	private var modifierFactory = ModifierCommandFactory()

	private val componentList = mutableListOf<ComponentFactory>()


	fun addModifier(factory: IModifierFactory) {
		this.modifierFactory.addModifier(factory)
	}

	fun addComponent(componentFactory: ComponentFactory) {
		this.componentList.add(componentFactory)
	}


	fun createWithBody(): Entity {
		val bodyBuilder = requireNotNull(bodyBuilder) { "BodyBuilder needs to be initialized." }
		return createWithBody(Graphics.getContainer(bodyBuilder.motionType))
	}

	fun createWithBody(container: Container): Entity {
		return EntityManager.createEntity {
			buildBody(this, container, requireNotNull(bodyBuilder), it)
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

			if (isPlayer) {
				addComponent(PlayerComponent())
				addComponent(AccelerationComponent(Double2(2.0, 6.8)))
			}

			if (canReceiveModifiers) {
				addComponent(ModifierReceiverComponent(entity))
			}

			if (follow) {
				addComponent(DisplayFollowComponent())
			}

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

		if (usePhysics) {
			addPhysics(entityBuilder, bodyBuilder.buildBody(entity))
		}

		entityBuilder.addComponent(DefaultBodyComponent(bodyBuilder))
		entityBuilder.addComponent(BodyComponent(bodyBuilder))
	}

	private fun addGraphics(
		entityBuilder: EntityComponentsBuilder,
		container: Container,
		graphics: definition.jslib.pixi.Graphics
	) {
		container.addChild(graphics)
		entityBuilder.addComponent(GraphicsComponent(graphics))
	}

	private fun addPhysics(
		entityBuilder: EntityComponentsBuilder,
		body: IBody
	) {
		entityBuilder.addComponent(PhysicsEntityComponent(body))

		when (body.motionType) {
			BodyMotionType.Dynamic -> entityBuilder.addComponent(PhysicsDynamicEntityComponent())
			BodyMotionType.Kinematic -> entityBuilder.addComponent(PhysicsKinematicEntityComponent())
			else -> {
			}
		}
	}


	companion object {
		inline fun createWithBody(
			container: Container,
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).createWithBody(container)
		}

		inline fun createWithBody(
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).createWithBody()
		}

		inline fun createWithoutBody(
			func: EntityCreator.() -> Unit
		): Entity {
			return EntityCreator().apply(func).createWithoutBody()
		}
	}
}