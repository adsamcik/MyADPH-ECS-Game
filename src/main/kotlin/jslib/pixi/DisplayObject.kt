@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

import jslib.pixi.interaction.InteractionEvent
import jslib.pixi.utils.EventEmitter

open external class DisplayObject : EventEmitter {
	var alpha: Double
	var buttonMode: Boolean
	var filters: Array<jslib.pixi.Filter>
	var height: Double
	var interactive: Boolean
	var mask: Mask?
	val parent: jslib.pixi.Container
	var pivot: IPoint
	var position: IPoint
	var rotation: Double
	val scale: IPoint
	val skew: ObservablePoint
	var width: Double
	var x: Double
	var y: Double
	var hitArea: IHitArea

	var zIndex: Int
	var visible: Boolean

	val worldAlpha: Double
	val worldVisible: Boolean

	fun destroy()
	fun getBounds(skipUpdate: Boolean = definedExternally, rect: Rectangle = definedExternally): Rectangle
	fun getLocalBounds(rect: Rectangle = definedExternally): Rectangle
}