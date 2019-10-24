@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("mesh")

package definition.jslib.pixi.mesh

import definition.jslib.pixi.DisplayObject
import definition.jslib.pixi.PIXI
import definition.jslib.pixi.Point
import definition.jslib.pixi.Texture

open external class Rope(texture: Texture, points: Array<Point>) : DisplayObject