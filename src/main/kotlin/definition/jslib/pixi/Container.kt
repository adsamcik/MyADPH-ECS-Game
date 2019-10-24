@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

open external class Container : DisplayObject {
	open fun addChild(child: DisplayObject)
	open fun addChildAt(child: DisplayObject, index: Int)
	open fun removeChild(child: DisplayObject): DisplayObject
	open fun removeChildAt(index: Int): DisplayObject
	open fun removeChildren(
		beginIndex: Number = definedExternally,
		endIndex: Number = definedExternally
	): Array<DisplayObject>

	open fun getChildIndex(child: DisplayObject): Int
	open fun getChildByName(name: String): DisplayObject

	val children: Array<definition.jslib.pixi.DisplayObject>
}