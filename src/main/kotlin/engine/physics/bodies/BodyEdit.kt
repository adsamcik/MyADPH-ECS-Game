package engine.physics.bodies

import ecs.components.BodyComponent
import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.physics.IShape
import engine.physics.bodies.builder.MutableBodyBuilder
import utility.Rgba

object BodyEdit {
	fun setShape(entity: Entity, shape: IShape) {
		val bodyComponent = entity.getComponent(BodyComponent::class)

		if (bodyComponent.value.shape == shape)
			return

		val physicsComponent = entity.getComponent(PhysicsEntityComponent::class)

		val bodyBuilder = MutableBodyBuilder(shape, physicsComponent.body).apply {
			fillColor = bodyComponent.value.fillColor
		}
		setBody(entity, bodyBuilder)
	}

	fun setBody(entity: Entity, bodyBuilder: MutableBodyBuilder) {
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
		val bodyBuilder = entity.getComponent(BodyComponent::class).value

		bodyBuilder.apply {
			this.fillColor = color
		}

		val graphics = bodyBuilder.buildGraphics()
		Graphics.getContainer(bodyBuilder.motionType).addChild(graphics)

		EntityManager.setComponent(entity, GraphicsComponent(graphics))
	}
}