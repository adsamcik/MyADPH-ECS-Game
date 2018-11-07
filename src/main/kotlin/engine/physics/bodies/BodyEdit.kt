package engine.physics.bodies

import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.Graphics
import engine.entity.Entity
import engine.entity.EntityManager
import engine.physics.IShape

object BodyEdit {
	fun setShape(entity: Entity, shape: IShape) {
		val bodyBuilder = BodyBuilder(shape)
		setShape(entity, bodyBuilder)
	}

	fun setShape(entity: Entity, bodyBuilder: BodyBuilder) {
		val physicsComponent = entity.getComponent(PhysicsEntityComponent::class)

		if(physicsComponent.shape == bodyBuilder.shape)
			return

		val body = bodyBuilder.buildBody(entity)
		val graphics = bodyBuilder.buildGraphics()

		val state = physicsComponent.saveProperties()

		body.restore(state)

		Graphics.getContainer(body.motionType).addChild(graphics)

		EntityManager.setComponents(
			entity,
			GraphicsComponent(graphics),
			PhysicsEntityComponent(body, bodyBuilder.shape)
		)
	}
}