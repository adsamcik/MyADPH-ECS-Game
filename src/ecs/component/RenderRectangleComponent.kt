package ecs.component

import engine.component.IComponent
import utility.Rgba

data class RenderRectangleComponent(val width: Double, val height: Double, val color: Rgba) : IComponent