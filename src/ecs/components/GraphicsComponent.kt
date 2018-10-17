package ecs.components

import engine.component.IMessyComponent

data class GraphicsComponent(val value: PIXI.DisplayObject) : IMessyComponent {
	override fun cleanup() {
		value.parent.removeChild(value)
	}

}