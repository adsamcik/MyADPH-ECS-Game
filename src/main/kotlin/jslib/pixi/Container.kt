@file:JsModule(PIXI)
@file:JsNonModule

package jslib.pixi

open external class Container : DisplayObject {
	open fun addChild(child: DisplayObject)
	open fun addChildAt(child: DisplayObject, index: Int)
	open fun removeChild(child: DisplayObject): DisplayObject
	open fun removeChildAt(index: Int): DisplayObject
	open fun removeChildren(beginIndex: Number = definedExternally, endIndex: Number = definedExternally)

	open fun getChildIndex(child: DisplayObject): Int
	open fun getChildByName(name: String): DisplayObject

	val children: Array<jslib.pixi.DisplayObject>
}