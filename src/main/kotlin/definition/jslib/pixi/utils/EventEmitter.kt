@file:JsModule(PIXI)
@file:JsNonModule
@file:JsQualifier("utils")

package definition.jslib.pixi.utils

import definition.jslib.pixi.PIXI

open external class EventEmitter {
	fun on(event: String, function: PixiEventListener, context: Any = definedExternally): EventEmitter
	fun once(event: String, function: PixiEventListener, context: Any = definedExternally): EventEmitter
	fun off(event: String = definedExternally, function: PixiEventListener = definedExternally, context: Any = definedExternally, once: Boolean = definedExternally): EventEmitter

	fun listeners(event: String): Array<PixiEventListener>
}

