@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("extras")

package definition.jslib.pixi.extras

import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.PIXI
import definition.jslib.pixi.Texture

open external class AnimatedSprite(frames: Array<Texture>) : DisplayObject {
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