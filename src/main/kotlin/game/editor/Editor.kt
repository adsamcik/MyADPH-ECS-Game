package game.editor

import debug.Debug
import debug.DebugLevel
import definition.Object
import definition.constant.EventConstants
import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.Point
import definition.jslib.pixi.Rectangle
import definition.jslib.pixi.interaction.InteractionEvent
import ecs.components.*
import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
import ecs.components.modifiers.ModifierReceiverComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import ecs.components.physics.PhysicsDynamicEntityComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.physics.PhysicsKinematicEntityComponent
import ecs.components.triggers.CheckpointComponent
import ecs.components.triggers.CheckpointType
import engine.component.IComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.graphics.Graphics
import engine.physics.bodies.BodyEdit
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.BodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.types.Rgba
import engine.types.Transform
import extensions.*
import game.levels.Level
import general.Double2
import general.Int2
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import kotlinx.serialization.stringify
import org.w3c.dom.Element
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Node
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.js.json
import kotlin.math.PI

class Editor : Level("Editor") {
	override val isGameLevel: Boolean = false

	private var mouseLastPosition: Int2 = Int2()

	private var selected: SelectedEntityData? = null
	private var selectionHighlight = definition.jslib.pixi.Graphics()

	private val scrollList = document.createDiv()

	private val availableComponentList = listOf(
		{ DamageComponent(100.0) },
		{ HealthComponent(100.0) },
		{ DisplayFollowComponent() },
		{ CheckpointComponent(0, Double2(), CheckpointType.Standard) },
		{ EnergyComponent(100.0, 10.0, 5.0) },
		{ PlayerComponent() },
		{ RotateMeComponent(1.0) },
		{ LifeTimeComponent(100.0) }
	)

	private val editUI = EditUIManager()

	override fun loadLevel() {
		initUI()
		Graphics.worldUIContainer.addChild(selectionHighlight)

		addEvents()
		//Graphics.pixi.stage.on("pointerdown") {}
	}

	private fun addEvents() {
		Graphics.staticForegroundContainer.interactive = true
		Graphics.staticForegroundContainer.on("click", {
			val localPosition = it.data.getLocalPosition(Graphics.staticForegroundContainer)
			val bounds = Rectangle(0, 0, 0, 0)
			Graphics.staticForegroundContainer.children.forEach { child ->
				val localBounds = child.getLocalBounds(rect = bounds)
				localBounds.x += child.x
				localBounds.y += child.y
				if (localBounds.contains(localPosition.x, localPosition.y)) {
					val entity = EntityManager.getEntityByComponent(GraphicsComponent(child))
					val physicsEntityComponent = entity.getComponent<PhysicsEntityComponent>()
					val selectedData = SelectedEntityData(entity, physicsEntityComponent, child)
					Debug.log(DebugLevel.ALL, "Clicked on", entity, child)
					select(selectedData)
					return@forEach
				}
			}
		})

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

		val export = createMenuButton {
			it.textContent = "Export level"
			it.addEventListener(EventConstants.CLICK, {
				val exported = EntityManager.serialize()
				window.alert(exported)
			})
		}.also {
			ul.appendChild(it)
		}

		return ul
	}

	private fun onItemMove(entityData: SelectedEntityData, mouseScaledOffset: Double2) {
		entityData.physicsComponent.body.position -= mouseScaledOffset
		BodyEdit.setPosition(entityData.entity, entityData.physicsComponent.body.position)
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
		selected?.displayObject?.let {
			it.off("pointerdown")
			it.off("pointerup")
			it.off("pointermove")
			it.interactive = false
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
				bounds.width + SELECTION_OUTLINE_OFFSET * 2,
				bounds.height + SELECTION_OUTLINE_OFFSET * 2
			)
			selectionHighlight.position.set(targetDisplayObject.x, targetDisplayObject.y)

			entityData.displayObject.let {
				it.on("pointerdown", this::onPointerDown)
				it.on("pointerup", this::onPointerUp)
				it.interactive = true
			}
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
		availableComponentList
			.map { it.invoke() }
			.filter { a -> componentList.none { b -> a::class == b::class } }
			.forEach { component ->
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

	private fun switchToEdit(entityData: SelectedEntityData) {
		scrollList.removeAllChildren()
		EntityManager.getComponentsList(entityData.entity).forEach {
			val componentContainer = editUI.createUIFor(entityData.entity, it)

			if (componentContainer != null) scrollList.appendChild(componentContainer)
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