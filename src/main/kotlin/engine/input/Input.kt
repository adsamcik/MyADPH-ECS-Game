package engine.input

import engine.events.UpdateManager
import engine.events.IUpdatable
import definition.jslib.ZingTouch
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document

object Input : IUpdatable {
	private val immediateState = InputState()
	private val frameState = InputState()

	init {
		document.addEventListener("keydown", Input::keyDownHandler, false)
		document.addEventListener("keyup", Input::keyUpHandler, false)

		val body = document.body!!
		val touchRegion = ZingTouch.Region(body)
		touchRegion.bind(body)
			.swipe(Input::swipeHandler)
			.pan(Input::panHandler)

		UpdateManager.subscribe(this)
	}

	private fun swipeHandler(event: ZingTouch.GestureEvent<ZingTouch.Swipe.EventData>) {
		immediateState.registerSwipe(event.detail.data[0])
	}

	private fun panHandler(event: ZingTouch.GestureEvent<ZingTouch.Pan.EventData>) {
		immediateState.registerPan(event.detail.data[0])
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

	fun getKeyState(key: String) = frameState.getState(key)

	fun horizontal(): Double {
		val left = this.left
		val right = this.right

		val leftDown = left.isDown()
		val rightDown = right.isDown()

		return if (leftDown) {
			if (rightDown) 0.0 else -1.0
		} else if (rightDown) {
			1.0
		} else {
			0.0
		}
	}

	fun vertical(): Double {
		val up = this.up
		val down = this.down

		val upDown = up.isDown()
		val downDown = down.isDown()

		return if (upDown) {
			if (downDown) 0.0 else -1.0
		} else if (downDown) {
			1.0
		} else {
			0.0
		}
	}


	val left
		get() = frameState.getState(A, LEFT)

	val up
		get() = frameState.getState(W, UP)

	val right
		get() = frameState.getState(D, RIGHT)

	val down
		get() = frameState.getState(S, DOWN)


	//Gestures

	val isPanning
		get() = frameState.gestures.pan != null

	val pan
		get() = frameState.gestures.pan
			?: throw NullPointerException("There is no active pan gesture. Check isPanning variable first")

	val hasSwiped
		get() = frameState.gestures.swipe != null

	val swipeData: ZingTouch.Swipe.EventData
		get() = frameState.gestures.swipe
			?: throw NullPointerException("There is no active swipe gesture. Check hasSwiped variable first")


	private const val A = "KeyA"
	private const val S = "KeyS"
	private const val D = "KeyD"
	private const val W = "KeyW"
	private const val UP = "ArrowUp"
	private const val DOWN = "ArrowDown"
	private const val LEFT = "ArrowLeft"
	private const val RIGHT = "ArrowRight"

	const val ESCAPE = "Escape"
}