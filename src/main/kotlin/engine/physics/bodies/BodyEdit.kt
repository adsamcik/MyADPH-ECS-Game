package engine.physics.bodies

import ecs.components.DefaultBodyComponent
import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.physics.IShape
import engine.physics.bodies.builder.MutableBodyBuilder

object BodyEdit {
	fun setShape(entity: Entity, shape: IShape) {
		val bodyComponent = entity.getComponent(DefaultBodyComponent::class)

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
			DefaultBodyComponent(bodyBuilder)
		)
	}
}