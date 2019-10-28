package ecs.components.template

import engine.component.IComponent
import engine.physics.bodies.builder.IBodyBuilder

interface IBodyComponent : IComponent {
	val value: IBodyBuilder
}