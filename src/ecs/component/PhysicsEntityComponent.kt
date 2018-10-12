package ecs.component

import Matter
import engine.component.IComponent

class PhysicsEntityComponent(val body: Matter.Body) : IComponent