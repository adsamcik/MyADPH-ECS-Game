@file:JsModule("PIXI")
@file:JsNonModule

package PIXI

external open class Container : DisplayObject {
	fun addChild(child: DisplayObject)
	fun removeChildren(beginIndex: Number = definedExternally, endIndex: Number = definedExternally)
	val children: Array<PIXI.DisplayObject>
}