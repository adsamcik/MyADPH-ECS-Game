package ecs.components

import engine.component.IMessyComponent
import jslib.pixi.DisplayObject

data class GraphicsComponent(val value: DisplayObject) : IMessyComponent {
	override fun cleanup() {
		value.parent.removeChild(value)
	}
}