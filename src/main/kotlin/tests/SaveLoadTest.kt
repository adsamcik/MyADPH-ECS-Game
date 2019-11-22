package tests

import ecs.components.RotateMeComponent
import engine.entity.EntityManager
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.MutableBodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.types.Rgba
import game.editor.component.PlayerDefinitionComponent
import game.levels.EntityCreator
import game.levels.LevelManager
import general.Double2
import kotlin.math.PI

class SaveLoadTest : ITest {
	override fun run() {
		val defaultDefinition = "{}"
		LevelManager.loadCustomLevel(defaultDefinition)
		val createdEntity = EntityCreator.createWithBody {
			bodyBuilder = MutableBodyBuilder(Circle(PI), BodyMotionType.Dynamic).apply {
				fillColor = Rgba.FOREST_GREEN
				restitution = 5.1
				transform.position = Double2(1.0, 2.2)
				transform.angleDegrees = 41.5
			}
			addComponent { PlayerDefinitionComponent() }
			addComponent { RotateMeComponent(45.0) }
		}

		val definition = EntityManager.serialize()
		EntityManager.removeEntity(createdEntity)
		LevelManager.loadCustomLevel(defaultDefinition)
		EntityManager.deserialize(definition)
		val reload = EntityManager.serialize()

		/*val equals = definition == reload
		if (!equals) {
			console.log("first", definition, "second", reload)
			throw AssertionError("Level definitions differ after save/load.")
		}*/
	}

}