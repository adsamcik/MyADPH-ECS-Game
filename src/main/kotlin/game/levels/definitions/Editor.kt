package game.levels.definitions

import debug.Debug
import debug.DebugLevel
import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.graphics.Graphics
import engine.graphics.ui.Button
import engine.graphics.ui.ButtonConfig
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.BodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.types.Rgba
import engine.types.Transform
import game.levels.Level
import general.Double2
import general.Int2
import jslib.pixi.DisplayObject
import jslib.pixi.interaction.InteractionEvent
import org.w3c.dom.events.MouseEvent

class Editor : Level("Editor") {
	override val isGameLevel: Boolean = false

	private var mouseLastPosition: Int2 = Int2()

	private var selected: SelectedEntityData? = null
	private var selectionHighlight = jslib.pixi.Graphics()

	override fun loadLevel() {
		Graphics.levelUIContainer.addChild(
			Button(
				ButtonConfig(
					text = "New entity",
					position = Double2(x = Graphics.dimensions.x, y = 0.0),
					pivot = Double2(1.0, 0.0),
					onClickListener = this::createNewEntity
				)
			)
		)

		Graphics.dynamicContainer.addChild(selectionHighlight)

		//Graphics.pixi.stage.on("pointerdown") {}

		Graphics.backgroundUIContainer.run {
			on("pointerdown", {
				val event = it.data.originalEvent as MouseEvent
				mouseLastPosition = Int2(event.clientX, event.clientY)
				on("pointermove", this@Editor::onPan)
				select(null)
			})

			on("pointerup", {
				off("pointermove")
			})

			on("click", {
				Debug.log(DebugLevel.ALL, "Clicked on nothing")
				select(null)
			})
		}
	}

	private fun onItemMove(event: InteractionEvent) {
		console.log("moving")
		val selected = selected
		require(selected != null) { "Trying to move item while none is selected" }

		val origEvent = event.data.originalEvent as MouseEvent
		val newMousePosition = Int2(origEvent.clientX, origEvent.clientY)
		val scale = Graphics.scale
		val scaledOffset = Double2(
			(mouseLastPosition.x - newMousePosition.x) / scale,
			(mouseLastPosition.y - newMousePosition.y) / scale
		)
		mouseLastPosition = newMousePosition
		onItemMove(selected, scaledOffset)
	}

	private fun onItemMove(entityData: SelectedEntityData, mouseScaledOffset: Double2) {
		entityData.physicsComponent.body.position -= mouseScaledOffset
		selectionHighlight.position.set(
			selectionHighlight.x - mouseScaledOffset.x,
			selectionHighlight.y - mouseScaledOffset.y
		)
	}

	private fun onPan(event: InteractionEvent) {
		val it = event.data.originalEvent as MouseEvent
		val center = Graphics.center
		val scale = Graphics.scale
		Graphics.centerAt(
			Double2(
				center.x + (mouseLastPosition.x - it.clientX) / scale,
				center.y + (mouseLastPosition.y - it.clientY) / scale
			)
		)

		mouseLastPosition.x = it.clientX
		mouseLastPosition.y = it.clientY
	}

	private fun onPointerDown(event: InteractionEvent) {
		console.log("down")
		val mouseEvent = event.data.originalEvent as MouseEvent
		mouseLastPosition = Int2(mouseEvent.clientX, mouseEvent.clientY)
		requireNotNull(selected).displayObject.on("pointermove", this@Editor::onItemMove)
	}

	private fun onPointerUp(event: InteractionEvent) {
		console.log("up")
		requireNotNull(selected).displayObject.off("pointermove")
	}

	private fun select(entityData: SelectedEntityData?) {
		selected?.let {
			it.displayObject.off("pointerdown")
			it.displayObject.off("pointerup")
			it.displayObject.off("pointermove")
		}

		console.log("Selected", selected)

		selected = entityData

		selectionHighlight.clear()

		if (entityData != null) {
			val targetDisplayObject = entityData.displayObject
			val bounds = targetDisplayObject.getLocalBounds()
			selectionHighlight.lineStyle(2, Rgba.SKY_BLUE.rgb)
			//selectionHighlight.beginFill(color = Rgba.ORANGE.rgb)
			selectionHighlight.drawRect(bounds.x, bounds.y, bounds.width, bounds.height)
			selectionHighlight.position.set(targetDisplayObject.x, targetDisplayObject.y)
			console.log(bounds)

			entityData.displayObject.on("pointerdown", this::onPointerDown)
			entityData.displayObject.on("pointerup", this::onPointerUp)
			//selectionHighlight.endFill()
			Debug.log(DebugLevel.ALL, "Setting entityData to", entityData)
		} else {
			Debug.log(DebugLevel.ALL, "Clearing entityData")
		}
	}

	private fun createNewEntity(event: InteractionEvent) {
		val entity = createEntityWithBody {
			isPlayer = false
			bodyBuilder = BodyBuilder(
				BodyMotionType.Kinematic,
				Rgba.WHITE,
				Transform(Graphics.center, 0.0),
				Circle(10.0),
				0.0,
				0.0,
				false,
				null
			)
		}

		entity.getComponent<GraphicsComponent>().value.run {
			interactive = true
			on("click", {
				val physicsEntityComponent = entity.getComponent<PhysicsEntityComponent>()
				val selectedData = SelectedEntityData(entity, physicsEntityComponent, this)
				Debug.log(DebugLevel.ALL, "Clicked on", entity, it.target)
				select(selectedData)
			})
		}
	}
}

data class SelectedEntityData(
	val entity: Entity,
	val physicsComponent: PhysicsEntityComponent,
	val displayObject: DisplayObject
)