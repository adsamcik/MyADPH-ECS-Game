package engine.physics.bodies

import ecs.components.BodyComponent
import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.physics.bodies.builder.BodyBuilder
import engine.physics.bodies.builder.IBodyBuilder
import engine.physics.bodies.shapes.IShape
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.types.Rgba
import general.Double2

object BodyEdit {
	fun setShape(entity: Entity, shape: IShape) {
		val bodyComponent = entity.getComponent<BodyComponent>()

		if (bodyComponent.value.shape == shape) return

		val physicsComponent = entity.getComponent<PhysicsEntityComponent>()

		val bodyBuilder = MutableBodyBuilder(shape, physicsComponent.body).apply {
			fillColor = bodyComponent.value.fillColor
		}
		setBody(entity, bodyBuilder)
	}

	fun updateShape(entity: Entity) {
		val bodyComponent = entity.getComponent<BodyComponent>()
		setBody(entity, bodyComponent.value)
	}

	fun setBody(entity: Entity, bodyBuilder: IBodyBuilder) {
		val body = bodyBuilder.buildBody(entity)
		val graphics = bodyBuilder.buildGraphics()

		Graphics.getContainer(body.motionType).addChild(graphics)

		EntityManager.setComponents(
			entity,
			GraphicsComponent(graphics),
			PhysicsEntityComponent(body),
			BodyComponent(bodyBuilder)
		)
	}

	fun setColor(entity: Entity, color: Rgba) {
		val bodyComponent = entity.getComponent<BodyComponent>()
		val bodyBuilder = MutableBodyBuilder(bodyComponent.value)

		bodyBuilder.apply {
			this.fillColor = color
		}

		val graphics = bodyBuilder.buildGraphics()
		Graphics.getContainer(bodyBuilder.motionType).addChild(graphics)

		EntityManager.setComponents(entity, GraphicsComponent(graphics), BodyComponent(bodyBuilder))
	}

	fun setPosition(entity: Entity, position: Double2) {
		val bodyComponent = entity.getComponent<BodyComponent>()
		val bodyBuilder = MutableBodyBuilder(bodyComponent.value)

		bodyBuilder.apply {
			transform.position = position
		}

		EntityManager.setComponent(entity, BodyComponent(bodyBuilder))
	}
}