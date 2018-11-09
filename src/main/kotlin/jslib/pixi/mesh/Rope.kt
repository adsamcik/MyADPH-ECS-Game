@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("mesh")

package jslib.pixi.mesh

import jslib.pixi.DisplayObject
import jslib.pixi.PIXI
import jslib.pixi.Point
import jslib.pixi.Texture

open external class Rope(texture: Texture, points: Array<Point>) : DisplayObject