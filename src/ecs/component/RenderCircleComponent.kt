package ecs.component

import engine.component.IComponent
import utility.Rgba

data class RenderCircleComponent(val radius : Double, val color: Rgba) : IComponent