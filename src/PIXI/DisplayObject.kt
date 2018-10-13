@file:JsModule("pixi.js")
@file:JsNonModule

package PIXI

external open class DisplayObject {
    var alpha: Number
    val anchor: ObservablePoint
    var buttonMode: Boolean
    var filters: Array<PIXI.Filter>
    var height: Number
    var interactive: Boolean
    var mask: Mask?
    val parent: PIXI.Container
    val pivot: ObservablePoint
    var position: PIXI.Point
    var rotation: Number
    val scale: ObservablePoint
    var width: Number
    var x: Number
    var y: Number

    fun destroy()
    fun on(eventName: String, eventListener: (event: PIXI.interaction.InteractionEvent) -> Unit): DisplayObject
}