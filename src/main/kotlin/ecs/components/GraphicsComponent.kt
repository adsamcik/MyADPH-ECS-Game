package ecs.components

import engine.component.IMessyComponent
import definition.jslib.pixi.DisplayObject

data class GraphicsComponent(val value: DisplayObject) : IMessyComponent {
	override fun cleanup() {
		value.parent.removeChild(value)
	}
}