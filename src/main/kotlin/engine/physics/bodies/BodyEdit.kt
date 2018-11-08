package engine.physics.bodies

import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.graphics.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.IShape
import utility.Rgba

object BodyEdit {
	fun setShape(entity: Entity, shape: IShape) {
		val physicsComponent = entity.getComponent(PhysicsEntityComponent::class)

		if (physicsComponent.shape == shape)
			return

		val bodyBuilder = BodyBuilder(shape, physicsComponent.body).apply {
			fillColor = Rgba.RED
		}
		setBody(entity, bodyBuilder)
	}

	fun setBody(entity: Entity, bodyBuilder: BodyBuilder) {
		val body = bodyBuilder.buildBody(entity)
		val graphics = bodyBuilder.buildGraphics()

		Graphics.getContainer(body.motionType).addChild(graphics)

		EntityManager.setComponents(
			entity,
			GraphicsComponent(graphics),
			PhysicsEntityComponent(body, bodyBuilder.shape)
		)
	}
}