package ecs.components

import engine.component.IMessyComponent
import definition.jslib.pixi.DisplayObject
import engine.component.IGeneratedComponent

data class GraphicsComponent(val value: DisplayObject) : IMessyComponent, IGeneratedComponent {
	override fun cleanup() {
		val parent = value.parent
		if (parent != null) {
			parent.removeChild(value)
		}
	}
}