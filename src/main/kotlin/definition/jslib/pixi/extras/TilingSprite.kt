@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("extras")

package definition.jslib.pixi.extras

import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.ObservablePoint
import definition.jslib.pixi.PIXI
import definition.jslib.pixi.Texture

open external class TilingSprite(texture: Texture, width: Number = definedExternally, height: Number = definedExternally) : DisplayObject {
	val tilePosition: ObservablePoint
	val tileScale: ObservablePoint
}