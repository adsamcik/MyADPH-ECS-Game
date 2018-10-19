@file:JsModule("PIXI")
@file:JsNonModule
@file:JsQualifier("extras")

package jslib.pixi.extras

import jslib.pixi.DisplayObject
import jslib.pixi.ObservablePoint
import jslib.pixi.Texture

external open class TilingSprite(texture: Texture, width: Number = definedExternally, height: Number = definedExternally) : DisplayObject {
	val tilePosition: ObservablePoint
	val tileScale: ObservablePoint
}