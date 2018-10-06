package ecs.component

import engine.component.IComponent
import org.w3c.dom.ImageBitmap

data class SpriteComponent(val image: ImageBitmap) : IComponent {

}