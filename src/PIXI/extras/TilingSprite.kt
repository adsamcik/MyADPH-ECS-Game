@file:JsModule("pixi.js")
@file:JsNonModule
@file:JsQualifier("extras")

package PIXI.extras

import PIXI.DisplayObject
import PIXI.ObservablePoint
import PIXI.Texture

external open class TilingSprite(texture: Texture, width: Number = definedExternally, height: Number = definedExternally) : DisplayObject {
    val tilePosition: ObservablePoint
    val tileScale: ObservablePoint
}