@file:JsModule("pixi.js")
@file:JsNonModule
@file:JsQualifier("mesh")

package PIXI.mesh

import PIXI.DisplayObject
import PIXI.Point
import PIXI.Texture

external open class Rope(texture: Texture, points: Array<Point>) : DisplayObject