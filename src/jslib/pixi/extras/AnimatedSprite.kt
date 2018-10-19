@file:JsModule("pixi")
@file:JsNonModule
@file:JsQualifier("extras")

package jslib.pixi.extras

import jslib.pixi.DisplayObject
import jslib.pixi.Texture

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