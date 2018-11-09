@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Container : DisplayObject {
	fun addChild(child: DisplayObject)
	fun addChildAt(child: DisplayObject, index: Int)
	fun removeChild(child: DisplayObject): DisplayObject
	fun removeChildAt(index: Int): DisplayObject
	fun removeChildren(beginIndex: Number = definedExternally, endIndex: Number = definedExternally)

	fun getChildIndex(child: DisplayObject): Int
	fun getChildByName(name: String): DisplayObject

	val children: Array<jslib.pixi.DisplayObject>
}