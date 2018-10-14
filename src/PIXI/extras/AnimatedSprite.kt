@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("extras")

package PIXI.extras

import PIXI.DisplayObject
import PIXI.Texture

external open class AnimatedSprite(frames: Array<Texture>) : DisplayObject {
	interface Frame {
		val texture: Texture
		val time: Number
	}

	constructor(frames: Array<Frame>)

	var animationSpeed: Number

	fun gotoAndPlay(frameNumber: Number)
	fun gotoAndStop(frameNumber: Number)
	fun play()
	fun stop()
}