@file:JsModule("pixi")
@file:JsNonModule
@file:JsQualifier("mesh")

package jslib.pixi.mesh

import jslib.pixi.DisplayObject
import jslib.pixi.Point
import jslib.pixi.Texture

open external class Rope(texture: Texture, points: Array<Point>) : DisplayObject