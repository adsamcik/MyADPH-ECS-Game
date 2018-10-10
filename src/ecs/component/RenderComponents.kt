package ecs.component

import engine.component.IComponent
import utility.Rgba

interface IRenderComponent : IComponent

data class RenderCircleComponent(val radius : Double, val color: Rgba) : IRenderComponent

data class RenderRectangleComponent(val width: Double, val height: Double, val color: Rgba) : IRenderComponent