package game.editor

import debug.Debug
import debug.DebugLevel
import definition.constant.EventConstants
import definition.jslib.Swal
import definition.jslib.SwalData
import definition.jslib.pixi.Container
import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.Rectangle
import definition.jslib.pixi.interaction.InteractionEvent
import ecs.components.*
import ecs.components.health.DamageComponent
import ecs.components.health.HealthComponent
import ecs.components.health.InstantDestructionComponent
import ecs.components.modifiers.ModifierSpreaderComponent
import ecs.components.physics.PhysicsEntityComponent
import ecs.components.template.IBodyComponent
import ecs.system.DisplayFollowSystem
import ecs.system.RoundAndRoundWeGoSystem
import engine.component.IGeneratedComponent
import engine.entity.Entity
import engine.entity.EntityManager
import engine.events.UpdateManager
import engine.graphics.Graphics
import engine.physics.Physics
import engine.physics.bodies.BodyEdit
import engine.physics.bodies.BodyMotionType
import engine.physics.bodies.builder.BodyBuilder
import engine.physics.bodies.shapes.Circle
import engine.serialization.EntitySerializer
import engine.system.SystemManager
import engine.types.Rgba
import engine.types.Transform
import extensions.addOnClickListener
import extensions.createDiv
import extensions.createSpan
import extensions.removeAllChildren
import game.editor.component.CheckpointDefinitionComponent
import game.editor.component.PlayerDefinitionComponent
import game.editor.system.EditorShortcutSystem
import game.levels.Level
import game.modifiers.ModifierCommandFactory
import general.Double2
import general.Int2
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.js.json

class Editor : Level("Editor") {
	override val isGameLevel: Boolean = false

	private var mouseLastPosition: Int2 = Int2()

	private var selected: SelectedEntityData? = null
	private var selectionHighlight = definition.jslib.pixi.Graphics()

	private var selectedEditorTab = EditorTab.None

	private val scrollList = document.createDiv()

	private val shortcutSystem =
		EditorShortcutSystem(
			this::requestSelectedEntityRemoval,
			this::requestSelectedEntityCopy,
			this::requestPaste
		)

	private val availableComponentList = listOf(
		{ DamageComponent(100.0) },
		{ HealthComponent(100.0) },
		{ CheckpointDefinitionComponent(0, Double2()) },
		{ EnergyComponent(100.0, 10.0, 5.0) },
		{ PlayerDefinitionComponent() },
		{ RotateMeComponent(1.0) },
		{ LifeTimeComponent(100.0) },
		{ AccelerationComponent(Double2(2.0, 6.8)) },
		{ ModifierSpreaderComponent(ModifierCommandFactory()) },
		{ DisplayFollowComponent() },
		{ InstantDestructionComponent() }
	)

	private var copyMemory: String? = null

	private val editUI = EditUIManager()

	override fun loadLevel() {
		initUI()
		Graphics.worldUIContainer.addChild(selectionHighlight)

		addEvents()

		UpdateManager.subscribePre(shortcutSystem)
		SystemManager.unregisterSystem(DisplayFollowSystem::class)
		SystemManager.unregisterSystem(RoundAndRoundWeGoSystem::class)
	}

	override fun unloadLevel() {
		UpdateManager.unsubscribePre(shortcutSystem)
		SystemManager.registerSystems(
			DisplayFollowSystem() to 100,
			RoundAndRoundWeGoSystem() to 0
		)
	}

	private fun onItemClick(container: Container, event: InteractionEvent) {
		val bounds = Rectangle(0, 0, 0, 0)
		container.children.forEach { child ->
			val localPosition = event.data.getLocalPosition(child)
			val localBounds = child.getLocalBounds(rect = bounds)

			if (localBounds.contains(localPosition.x, localPosition.y)) {
				val entity = EntityManager.getEntityByComponent(GraphicsComponent(child))
				Debug.log(DebugLevel.ALL, "Clicked on", entity, child)
				val selectedData = SelectedEntityData(entity, displayObject = child)
				select(selectedData)
				return@forEach
			}
		}
	}

	private fun addEvents() {
		arrayOf(
			Graphics.staticForegroundContainer,
			Graphics.staticBackgroundContainer,
			Graphics.dynamicContainer
		).forEach { container ->
			container.interactive = true
			container.on("click", { onItemClick(container, it) })
		}

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

	private fun initInspector(): HTMLElement {
		val inspectorUI = document.createDiv()
		val position = Graphics.pixi.screen
		inspectorUI.addClass("html-edit-inspector")

		val body = requireNotNull(document.body)

		body.insertBefore(inspectorUI, body.firstChild)

		inspectorUI.appendChild(scrollList)
		return inspectorUI
	}

	private fun initTools(inspector: HTMLElement): HTMLElement {
		return document.createDiv().apply {
			addClass("html-edit-tools")
			appendChild(createMenu())
		}.also {
			val body = requireNotNull(document.body)
			body.insertBefore(it, body.firstChild)
		}
	}

	private fun initUI() {
		val inspector = initInspector()
		initTools(inspector)
	}

	private fun createButton(className: String, init: (button: Element) -> Unit): Element {
		val li = document.createElement("li")
		document.createElement("button").apply {
			init(this)
			this.className = className
			li.appendChild(this)
		}
		return li
	}

	private fun createMenuButton(init: (button: Element) -> Unit): Element {
		return createButton("ui-button", init)
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
			it.textContent = "Remove entity"
			it.addEventListener(EventConstants.CLICK, { requestSelectedEntityRemoval() })
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Simulate selected entity"
			it.addEventListener(EventConstants.CLICK, {
				selected?.run { simulate(this) }
			})
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Add component"
			it.addEventListener(EventConstants.CLICK, { switchToAdd() })
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Edit component"
			it.addEventListener(EventConstants.CLICK, { switchToEdit() })
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Delete component"
			it.addEventListener(EventConstants.CLICK, { switchToRemove() })
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Export level"
			it.addEventListener(EventConstants.CLICK, {
				val exported = EntitySerializer.serialize()
				Swal.fire(
					SwalData(
						title = "Export level definition",
						html = "<textarea class='fullsized' readonly></textarea>",
						onBeforeOpen = { element ->
							val textArea = element.getElementsByTagName("textarea")[0] as HTMLTextAreaElement
							textArea.value = exported
						})
				)
			})
		}.also {
			ul.appendChild(it)
		}

		createMenuButton {
			it.textContent = "Import level"
			it.addEventListener(EventConstants.CLICK, {
				Swal.fire(
					SwalData(
						title = "JSON level definition",
						input = "text",
						inputValidator = { input ->
							if (input.isBlank()) {
								return@SwalData "Input cannot be empty"
							}

							try {
								EntitySerializer.deserialize(input).forEach { entity ->
									EntityManager.tryGetComponent(entity, PhysicsEntityComponent::class)?.apply {
										body.motionType = BodyMotionType.Kinematic
									}
								}
								null
							} catch (e: Exception) {
								e.message
							}
						}
					)
				)
			})
		}.also {
			ul.appendChild(it)
		}

		return ul
	}

	private fun simulate(entityData: SelectedEntityData) {
		val body = entityData.physicsComponent.body
		body.motionType = BodyMotionType.Dynamic
		body.isAwake = true

		val delta = 1.0 / 60.0
		var maxTime = 10.0
		do {
			maxTime -= delta
			Physics.engine.update(delta)
		} while (maxTime >= 0 && body.isAwake)

		body.motionType = BodyMotionType.Kinematic
		entityData.entity.getComponent<BodyComponent>().value.apply {
			transform.position = body.position
			transform.angleRadians = body.angleRadians
		}
		body.isAwake = false
		UpdateManager.update(0.0)
		select(entityData)
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

	private fun requestSelectedEntityRemoval() {
		val selectedEntity = selected
		if (selectedEntity != null) {
			Swal.fire(
				SwalData(
					title = "Are you sure you want to remove entity $selectedEntity",
					showCancelButton = true,
					preConfirm = {
						EntityManager.removeEntity(selectedEntity.entity)
						select(null)
					}
				)
			)
		}
	}

	private fun requestSelectedEntityCopy() {
		val selectedEntity = selected
		if (selectedEntity != null) {
			copyMemory = EntitySerializer.serializeEntity(selectedEntity.entity)
		}
	}

	private fun requestPaste() {
		val copyMemory = copyMemory
		if (copyMemory != null) {
			val entityList = EntitySerializer.deserialize(copyMemory)
			if (entityList.size == 1) {
				select(SelectedEntityData(entityList.first()))
			}
		}
	}

	private fun select(entityData: SelectedEntityData?) {
		selected?.displayObject?.let {
			it.off("pointerdown")
			it.off("pointerup")
			it.off("pointermove")
			it.interactive = false
		}

		selected = entityData

		selectionHighlight.clear()

		if (entityData != null) {
			val targetDisplayObject = entityData.displayObject
			val bounds = targetDisplayObject.getLocalBounds()
			selectionHighlight.lineStyle(SELECTION_OUTLINE_SIZE, Rgba.SKY_BLUE.rgb)
			//selectionHighlight.beginFill(color = Rgba.ORANGE.rgb)
			selectionHighlight.drawRect(
				0,
				0,
				bounds.width + SELECTION_OUTLINE_OFFSET * 2,
				bounds.height + SELECTION_OUTLINE_OFFSET * 2
			)
			selectionHighlight.pivot.set(
				bounds.width / 2 + SELECTION_OUTLINE_OFFSET,
				bounds.height / 2 + SELECTION_OUTLINE_OFFSET
			)
			selectionHighlight.rotation = entityData.physicsComponent.body.angleRadians
			selectionHighlight.position.set(targetDisplayObject.x, targetDisplayObject.y)

			entityData.displayObject.let {
				it.on("pointerdown", this::onPointerDown)
				it.on("pointerup", this::onPointerUp)
				it.interactive = true
			}

			switchToTab(selectedEditorTab)
			//selectionHighlight.endFill()
			Debug.log(DebugLevel.ALL, "Setting entityData to", entityData)
		} else {
			switchToTab(EditorTab.None)
			Debug.log(DebugLevel.ALL, "Clearing entityData")
		}
	}

	private fun switchToEdit() {
		switchToTab(EditorTab.Edit)
	}

	private fun switchToAdd() {
		switchToTab(EditorTab.Add)
	}

	private fun switchToRemove() {
		switchToTab(EditorTab.Delete)
	}

	private fun switchToTab(tab: EditorTab) {
		selectedEditorTab = tab

		scrollList.removeAllChildren()
		when (tab) {
			EditorTab.None -> Unit
			EditorTab.Add -> selected?.let { createAddTab(it) }
			EditorTab.Edit -> selected?.let { createEditTab(it) }
			EditorTab.Delete -> selected?.let { createRemoveTab(it) }
		}
	}

	private fun addButton(parentNode: Node, text: String, onClickListener: (event: Event) -> Unit): Element {
		return document.createElement("button").also { button ->
			button.className = "button-item"
			button.addOnClickListener(onClickListener)
			document.createSpan {
				it.textContent = text
				button.appendChild(it)
			}

			parentNode.appendChild(button)
		}
	}

	private fun createAddTab(entityData: SelectedEntityData) {
		val componentList = EntityManager.getComponentsList(entityData.entity)
		availableComponentList
			.map { it.invoke() }
			.filter { a -> componentList.none { b -> a::class == b::class } }
			.forEach { component ->
				val name = requireNotNull(component::class.simpleName).removeSuffix("Component")

				addButton(scrollList, name) {
					EntityManager.addComponent(entityData.entity, component)
					switchToTab(EditorTab.Add)
				}
			}
	}

	private fun createEditTab(entityData: SelectedEntityData) {
		EntityManager.getComponentsList(entityData.entity).forEach {
			val componentContainer = editUI.createUIFor(entityData.entity, it)

			if (componentContainer != null) scrollList.appendChild(componentContainer)
		}
	}

	private fun createRemoveTab(entityData: SelectedEntityData) {
		EntityManager.getComponentsList(entityData.entity)
			.filter { it !is IGeneratedComponent && it !is IBodyComponent }
			.forEach { component ->
				val name = requireNotNull(component::class.simpleName).removeSuffix("Component")
				addButton(scrollList, name) {
					EntityManager.removeComponent(entityData.entity, component)
					switchToTab(EditorTab.Delete)
				}
			}
	}

	private fun createNewEntity() {
		val entity = createEntityWithBody {
			isPlayer = false
			bodyBuilder = BodyBuilder(
				BodyMotionType.Kinematic,
				Rgba.WHITE,
				Transform(Graphics.center, 0.0),
				Circle(5.0),
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
) {
	constructor(
		entity: Entity,
		physicsComponent: PhysicsEntityComponent? = null,
		displayObject: DisplayObject? = null
	) : this(
		entity,
		physicsComponent ?: entity.getComponent<PhysicsEntityComponent>(),
		displayObject ?: entity.getComponent<GraphicsComponent>().value
	)
}