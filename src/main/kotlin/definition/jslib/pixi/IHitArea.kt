@file:JsModule(PIXI)
@file:JsNonModule

package definition.jslib.pixi

external interface IHitArea {
	fun contains(x: Number, y: Number): Boolean
}