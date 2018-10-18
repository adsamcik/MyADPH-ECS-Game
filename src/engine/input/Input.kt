package engine.input

import engine.UpdateManager
import engine.interfaces.IUpdatable
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.TouchEvent
import kotlin.browser.document

object Input : IUpdatable {
	private val immediateState = InputState()
	private val frameState = InputState()

	init {
		document.addEventListener("keydown", Input::keyDownHandler, false)
		document.addEventListener("keyup", Input::keyUpHandler, false)

		document.addEventListener("mousedown", Input::mouseDownHandler, false)
		document.addEventListener("mouseup", Input::mouseUpHandler, false)

		document.addEventListener("touchstart", Input::mouseDownHandler, false)
		document.addEventListener("touchend", Input::mouseUpHandler, false)
		UpdateManager.subscribe(this)
	}

	private fun touchDown(event: TouchEvent) {
		val touch = event.touches[0]
		immediateState.registerPointDown(event.timeStamp as Long, )
	}

	private fun mouseDownHandler(event: Event) = mouseDown(event as MouseEvent)
	private fun mouseDown(event: MouseEvent) {
		immediateState.registerPointDown(event.timeStamp as Long, event.screenX, event.screenY)
	}

	private fun mouseUpHandler(event: Event) = mouseUp(event as MouseEvent)
	private fun mouseUp(event: MouseEvent) {
		immediateState.registerPointerUp(event.timeStamp as Long, event.screenX, event.screenY)
	}

	private fun keyDownHandler(event: Event) = keyDown(event as KeyboardEvent)
	private fun keyDown(event: KeyboardEvent) {
		immediateState.registerKeyDown(event.code)
	}

	private fun keyUpHandler(event: Event) = keyUp(event as KeyboardEvent)
	private fun keyUp(event: KeyboardEvent) {
		immediateState.registerKeyUp(event.code)
	}

	override fun update(deltaTime: Double) {
		frameState.update(immediateState)
	}

	fun horizontal(): Double {
		val left = this.left
		val right = this.right

		val leftDown = left.isDown()
		val rightDown = right.isDown()

		return if (leftDown) {
			if (rightDown)
				0.0
			else
				-1.0
		} else if (rightDown)
			1.0
		else
			0.0
	}

	fun vertical(): Double {
		val up = this.up
		val down = this.down

		val upDown = up.isDown()
		val downDown = down.isDown()

		return if (upDown) {
			if (downDown)
				0.0
			else
				-1.0
		} else if (downDown)
			1.0
		else
			0.0
	}


	val left
		get() = frameState.getState(A, LEFT)

	val up
		get() = frameState.getState(W, UP)

	val right
		get() = frameState.getState(D, RIGHT)

	val down
		get() = frameState.getState(S, DOWN)

	val mouse
		get() = frameState.pointer


	private const val A = "KeyA"
	private const val S = "KeyS"
	private const val D = "KeyD"
	private const val W = "KeyW"
	private const val UP = "ArrowUp"
	private const val DOWN = "ArrowDown"
	private const val LEFT = "ArrowLeft"
	private const val RIGHT = "ArrowRight"
}