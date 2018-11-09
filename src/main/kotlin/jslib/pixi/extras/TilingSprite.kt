@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("extras")

package jslib.pixi.extras

import jslib.pixi.DisplayObject
import jslib.pixi.ObservablePoint
import jslib.pixi.PIXI
import jslib.pixi.Texture

open external class TilingSprite(texture: Texture, width: Number = definedExternally, height: Number = definedExternally) : DisplayObject {
	val tilePosition: ObservablePoint
	val tileScale: ObservablePoint
}