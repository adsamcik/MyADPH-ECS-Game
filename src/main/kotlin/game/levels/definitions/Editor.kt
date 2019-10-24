package game.levels.definitions

import debug.Debug
import debug.DebugLevel
import definition.constant.EventConstants
import ecs.components.GraphicsComponent
import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
import ecs.components.physics.PhysicsEntityComponent
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
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
import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.interaction.InteractionEvent
import org.w3c.dom.events.MouseEvent
import definition.jslib.pixi.Container
import definition.jslib.pixi.Text
import definition.jslib.pixi.UI.TextInput
import extensions.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.js.json

class Editor : Level("Editor") {
	override val isGameLevel: Boolean = false

	private var mouseLastPosition: Int2 = Int2()

	private var selected: SelectedEntityData? = null
	private var selectionHighlight = definition.jslib.pixi.Graphics()

	private val scrollList = document.createDiv()

	private val availableComponentList = listOf({ DamageComponent(100.0) }, { HealthComponent(100.0) })

	override fun loadLevel() {
		initUI()
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

	private fun initUI() {
		val rootUI = document.createDiv()
		val position = Graphics.pixi.screen
		rootUI.asDynamic().style =
			"width: 300px;left:${position.right - 300}px"
		rootUI.addClass("html-ui")
		document.createElement("input").apply {
			asDynamic().value = "TEST"
			rootUI.appendChild(this)
		}

		val body = requireNotNull(document.body)

		body.insertBefore(rootUI, body.firstChild)

		rootUI.appendChild(createMenu())
		rootUI.appendChild(scrollList)
	}

	private fun createMenuButton(init: (button: Element) -> Unit): Element {
		val li = document.createElement("li")
		document.createElement("button").apply {
			init(this)
			li.appendChild(this)
		}
		return li
	}

	private fun createMenu(): Element {
		val ul = document.createElement("ul").apply {
			addClass("horizontal-menu")
		}

		createMenuButton {
			it.textContent = "Create entity"
			it.addEventListener(EventConstants.CLICK, { createNewEntity() })
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Add component"
			it.addEventListener(EventConstants.CLICK, { switchToAdd() })
		}.also {
			ul.appendChild(it)
		}

		val edit = createMenuButton {
			it.textContent = "Edit component"
			it.addEventListener(EventConstants.CLICK, { switchToEdit() })
		}.also {
			ul.appendChild(it)
		}

		val delete = createMenuButton {
			it.textContent = "Delete component"
			it.addEventListener(EventConstants.CLICK, { switchToRemove() })
		}.also {
			ul.appendChild(it)
		}

		return ul
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

	private fun switchToEdit() {
		selected?.let { switchToEdit(it) }
	}

	private fun switchToAdd() {
		selected?.let { switchToAdd(it) }
	}

	private fun switchToRemove() {
		selected?.let { switchToRemove(it) }
	}

	private fun addButton(parentNode: Node, init: Element.() -> Unit): Element {
		return document.createElement("button").apply(init).also { parentNode.appendChild(it) }
	}

	private fun switchToAdd(entityData: SelectedEntityData) {
		scrollList.removeAllChildren()
		val componentList = EntityManager.getComponentsList(entityData.entity)
		availableComponentList.map { it.invoke() }.filterNot { componentList.contains(it) }.forEach { component ->
			val name = requireNotNull(component::class.simpleName).removeSuffix("Component")

			addButton(scrollList) {
				textContent = name
				addOnClickListener {
					EntityManager.addComponent(entityData.entity, component)
					switchToAdd(entityData)
				}
			}
		}
	}

	private fun createEditForComponent(component: IComponent): Element {
		val container = document.createDiv()
		val result = js("Object.getOwnPropertyNames(component)") as Array<String>

		document.createTitle3 { it.innerHTML = requireNotNull(component::class.simpleName) }
		result.forEach {

			/*addButton(container) {
				textContent = name
				addOnClickListener {
					EntityManager.addComponent(entityData.entity, component)
					switchToAdd(entityData)
				}
			}
			container.addChild(
				TextInput(INPUT_STYLE).apply {
					placeholder = it
					text = component.asDynamic()[it].toString()
				}
			)*/
		}
		return container
	}

	private fun switchToEdit(entityData: SelectedEntityData) {
		scrollList.removeAllChildren()
		EntityManager.getComponentsList(entityData.entity).forEach {
			val componentContainer = createEditForComponent(it)
			scrollList.appendChild(componentContainer)
		}
	}

	private fun switchToRemove(entityData: SelectedEntityData) {
		scrollList.removeAllChildren()
		EntityManager.getComponentsList(entityData.entity).forEach { component ->
			val name = requireNotNull(component::class.simpleName).removeSuffix("Component")
			scrollList.appendChild(
				document.createButton {
					it.textContent = name
					it.addOnClickListener {
						EntityManager.removeComponent(entityData.entity, component)
						switchToRemove(entityData)
					}
				}
			)
		}
	}

	private fun createNewEntity() {
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

		private val INPUT_STYLE = json(
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
	}
}

data class SelectedEntityData(
	val entity: Entity,
	val physicsComponent: PhysicsEntityComponent,
	val displayObject: DisplayObject
)