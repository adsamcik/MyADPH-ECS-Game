package game.levels.definitions

import debug.Debug
import debug.DebugLevel
import ecs.components.GraphicsComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.entity.Entity
import engine.graphics.Graphics
import engine.graphics.ui.element.*
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
import general.Double2.Companion.set
import jslib.pixi.UI.TextInput
import kotlin.js.json

class Editor : Level("Editor") {
	override val isGameLevel: Boolean = false

	private var mouseLastPosition: Int2 = Int2()

	private var selected: SelectedEntityData? = null
	private var selectionHighlight = jslib.pixi.Graphics()

	override fun loadLevel() {
		Graphics.levelUIContainer.apply {
			val buttonList = UIList(Orientation.HORIZONTAL).apply {
				x = (Graphics.dimensions.x - 400).toDouble()
			}.apply {
				addChild(
					Button(
						ButtonConfig(
							text = "Edit",
							onClickListener = this@Editor::createNewEntity
						)
					)
				)
				addChild(
					Button(
						ButtonConfig(
							text = "Add",
							onClickListener = this@Editor::createNewEntity
						)
					)
				)
				addChild(
					Button(
						ButtonConfig(
							text = "New entity",
							onClickListener = this@Editor::createNewEntity
						)
					)
				)
			}

			addChild(buttonList)

			val scrollable = Scrollable().apply {
				position.set(Double2(x = Graphics.dimensions.x - 200, y = 40.0))
				setDimensions(200.0, Graphics.dimensions.y - 40.0)
				pivot.set(Double2(1.0, 0.0))
			}
			addChild(scrollable)

			val textInputStyle = json(
				"input" to json(
					"fontSize" to "16px",
					"padding" to "5px",
					"width" to "170px",
					"color" to "#FFFFFF"
				),
				"box" to json(
					"default" to json(
						"fill" to "0xAAAAAA",
						"rounded" to "12",
						"stroke" to json("color" to "0xCCCCCC", "width" to "3")
					)
				)
			)
			console.log(textInputStyle)
			val input = TextInput(textInputStyle)

			input.substituteText = "TEST VALUE"

			val list = UIList()

			list.addChild(input)

			for (i in 0..100) {
				/*list.addChild(
					Button(
						ButtonConfig(
							text = "New entity",
							isAutosized = false,
							dimensions = Double2(200, 20)
						)
					)
				)*/
			}

			scrollable.addChild(list)
		}

		Graphics.staticForegroundContainer.addChild(selectionHighlight)

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
		val mouseEvent = event.data.originalEvent as MouseEvent
		mouseLastPosition = Int2(mouseEvent.clientX, mouseEvent.clientY)
		requireNotNull(selected).displayObject.on("pointermove", this@Editor::onItemMove)
	}

	private fun onPointerUp(event: InteractionEvent) {
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
			selectionHighlight.lineStyle(SELECTION_OUTLINE_SIZE, Rgba.SKY_BLUE.rgb)
			//selectionHighlight.beginFill(color = Rgba.ORANGE.rgb)
			selectionHighlight.drawRect(
				bounds.x - SELECTION_OUTLINE_OFFSET,
				bounds.y - SELECTION_OUTLINE_OFFSET,
				bounds.width + SELECTION_OUTLINE_OFFSET,
				bounds.height + SELECTION_OUTLINE_OFFSET
			)
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

	companion object {
		private const val SELECTION_OUTLINE_SIZE = 1
		private const val SELECTION_OUTLINE_OFFSET = SELECTION_OUTLINE_SIZE / 2.0
	}
}

data class SelectedEntityData(
	val entity: Entity,
	val physicsComponent: PhysicsEntityComponent,
	val displayObject: DisplayObject
)