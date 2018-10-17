package ecs.components.modifiers

import engine.component.IComponent
import engine.physics.BodyBuilder

data class PendingModifierReceiverComponent(val bodyBuilder: BodyBuilder) : IComponent